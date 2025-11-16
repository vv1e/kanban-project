package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.models.Category;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.enums.SortingType;
import edu.sdccd.cisc190.kanban.ui.CategoryNameSetController;
import edu.sdccd.cisc190.kanban.ui.CategorySortController;
import edu.sdccd.cisc190.kanban.ui.KanbanController;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import edu.sdccd.cisc190.kanban.util.exceptions.LastCategoryDeletionException;
import edu.sdccd.cisc190.kanban.util.exceptions.RuntimeIOException;
import edu.sdccd.cisc190.kanban.util.interfaces.WindowSetup;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryBoxController {
    private Category category;

    @FXML VBox categoryRootBox;
    @FXML public Label categoryLabel;
    @FXML public ListView<Issue> categoryListView;

    @FXML
    public void initialize() {
        categoryListView.setCellFactory((issue -> {
            try {
                return new IssueCell();
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        }));

        // Remove ListView's children's focus when ListView loses focus.
        categoryListView.focusedProperty().addListener(this::listFocusLostListener);
    }

    public void setCategoryAndSortingProperty(Category category, ObjectProperty<SortingType> sortingType) {
        this.category = category;
        categoryLabel.textProperty().bind(category.nameProperty());

        final ObservableList<Issue> issues = category.getIssues();

        categoryListView.itemsProperty().bind(
            Bindings.createObjectBinding(
                () -> new SortedList<>(issues, sortingType.get().getComparator()),
                sortingType
            )
        );

        // Only make the list navigable to if it has children
        categoryListView.focusTraversableProperty().bind(
            Bindings.isNotEmpty(category.getIssues())
        );
    }

    @FXML
    private void onRenameAction() {
        class onRenameActionSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                CategoryNameSetController controller = fxmlLoader.getController();
                controller.setCategory(category);
            }
        }

        try {
            WindowHelper.loadWindowDisableMenuBar(
                CategoryNameSetController.class.getResource("category-name-change-view.fxml"),
                300, 150, false,
                "Rename Category",
                new onRenameActionSetup()
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("Rename Category", (Stage) KanbanApplication.getController().getMenuBar().getScene().getWindow());
        }
    }

    @FXML
    private void onRemoveAction() {
        final KanbanController controller = KanbanApplication.getController();
        final Stage stage = (Stage) controller.getMenuBar().getScene().getWindow();

        Alert alert = WindowHelper.createConfirmationAlert(
            Alert.AlertType.CONFIRMATION,
            "Confirm Category Deletion",
            """
                Are you sure you want to delete this category?
                WARNING: This action cannot be undone!
                """,
            stage
        );

        if (alert.getResult() == ButtonType.YES) {
            final Board board = controller.getBoard();
            final int categoryId = board.getCategories().indexOf(category);

            try {
                board.removeCategory(categoryId);
                controller.removeCategoryBox(categoryId);
            } catch (LastCategoryDeletionException ignored) {
                WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Cannot Delete Category",
                    "A Kanban Board must always have at least one category.",
                    stage
                );
            }
        }
    }

    @FXML
    private void onMoveAction() {
        final KanbanController kanbanController = KanbanApplication.getController();
        final Board board = kanbanController.getBoard();
        final int categoryId = board.getCategories().indexOf(category);

        class onMoveActionSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                CategorySortController controller = fxmlLoader.getController();
                controller.setCategory(categoryId);
            }
        }

        try {
            WindowHelper.loadWindowDisableMenuBar(
                CategorySortController.class.getResource("category-sort-view.fxml"),
                400, 250, true,
                "Move Category",
                new onMoveActionSetup()
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("Category Sort",  (Stage) KanbanApplication.getController().getMenuBar().getScene().getWindow());
        }
    }

    /*
     * Q: Why is this in CategoryBoxController and not IssueCell?
     * A: It is impossible to assign onKeyPressed to a selected ListCell in JavaFX - it always selects
     *     the first ListCell in a ListView. It's better to have the ListView handle onKeyPressed and
     *     then get the selected item from there.
     */
    @FXML
    private void onCellKeyPressedAction(KeyEvent event) {
        if (!event.isControlDown() && event.getCode() == KeyCode.ENTER) {
            // This opens the selected issue by pressing Enter/Return.
            final KanbanController controller = KanbanApplication.getController();

            Issue issue = categoryListView.getSelectionModel().getSelectedItem();
            controller.openIssue(issue);
        } else if (
            event.isShiftDown() &&  event.getCode() == KeyCode.F10 ||
            event.isControlDown() &&  event.getCode() == KeyCode.ENTER
        ) {
            // This opens the IssueCell's context menu if the user presses Shift+F10 (Windows/Linux)
            //  or Control+Return (macOS 15+)

            IssueCell selectedCell = getSelectedCell();
            if (selectedCell == null) return;

            double cellWidth = selectedCell.getWidth();

            double screenX = selectedCell.localToScreen(cellWidth+2, 0).getX();
            double screenY = selectedCell.localToScreen(0, 0).getY();

            ContextMenuEvent contextMenuEvent = new ContextMenuEvent(
                ContextMenuEvent.CONTEXT_MENU_REQUESTED,
                0, 0,
                screenX, screenY,
                true,
                null
            );

            selectedCell.fireEvent(contextMenuEvent);
        }
    }

    private IssueCell getSelectedCell() {
        for (Node node : categoryListView.lookupAll(".list-cell")) {
            IssueCell cell = (IssueCell) node;

            if (cell.isSelected() && cell.getItem() != null) {
                return cell;
            }
        }

        return null;
    }

    private void listFocusLostListener(ObservableValue<? extends Boolean> ignoredObservable, boolean ignoredOldValue, boolean isFocused) {
        if (!isFocused) {
            categoryListView.getSelectionModel().clearSelection();
        }
    }
}
