package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.enums.SortDirection;
import edu.sdccd.cisc190.kanban.enums.SortField;
import edu.sdccd.cisc190.kanban.models.Category;
import edu.sdccd.cisc190.kanban.enums.SortingType;
import edu.sdccd.cisc190.kanban.ui.components.CategoryBoxController;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import edu.sdccd.cisc190.kanban.util.exceptions.RuntimeIOException;
import edu.sdccd.cisc190.kanban.util.interfaces.WindowSetup;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class KanbanController {
    private Board board;
    private final BooleanProperty isBoardLoaded = new SimpleBooleanProperty(false);
    private final ObjectProperty<SortingType> sortingType = new SimpleObjectProperty<>(SortingType.NONE);

    @FXML private MenuBar menuBar;
    @FXML private Label welcomeLabel;
    @FXML private HBox kanbanColumns;
    @FXML private Label welcomeSubtitleLabel;

    @FXML private MenuItem menuFileNewCategory;
    @FXML private MenuItem menuFileNewIssue;
    @FXML private MenuItem menuFileSave;
    @FXML private MenuItem menuFileSaveAs;
    @FXML private MenuItem menuFileClose;

    @FXML private ToggleGroup menuSortFieldToggleGroup;
    @FXML private ToggleGroup menuSortDirectionToggleGroup;

    @FXML
    protected void initialize() {
        // macOS: This makes the menu bar use the system one instead of the JavaFX one.
        menuBar.setUseSystemMenuBar(System.getProperty("os.name").toLowerCase().contains("mac"));

        // Several categories are supposed to be inaccessible when there is no board loaded.
        MenuItem[] boardDependentMenus = new MenuItem[]{
            menuFileNewCategory,
            menuFileNewIssue,
            menuFileSave,
            menuFileSaveAs,
            menuFileClose
        };

        for (MenuItem menuItem : boardDependentMenus) {
            menuItem.disableProperty().bind(isBoardLoaded.not());
        }

        welcomeSubtitleLabel.visibleProperty().bind(isBoardLoaded.not());
        welcomeSubtitleLabel.managedProperty().bind(isBoardLoaded.not());

        kanbanColumns.visibleProperty().bind(isBoardLoaded);
        kanbanColumns.managedProperty().bind(isBoardLoaded);

        menuSortFieldToggleGroup.selectedToggleProperty().addListener(this::onMenuSortModified);
        menuSortDirectionToggleGroup.selectedToggleProperty().addListener(this::onMenuSortModified);
    }

    private void onMenuSortModified(ObservableValue<? extends Toggle> ignoredObservable, Toggle ignoredOldValue, Toggle ignoredNewValue) {
        final SortField field = SortField.valueOf((String) menuSortFieldToggleGroup.getSelectedToggle().getUserData());
        final SortDirection direction = SortDirection.valueOf((String) menuSortDirectionToggleGroup.getSelectedToggle().getUserData());

        sortingType.set(SortingType.from(field, direction));
    }

    @FXML
    private void menuNewBoard() {
        try {
            WindowHelper.loadWindow(
                BoardCreationController.class.getResource("board-creation-view.fxml"),
                300, 165, false,
                "New Board"
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("New Board", (Stage) menuBar.getScene().getWindow());
        }
    }

    @FXML
    private void menuNewCategory() {
        try {
            WindowHelper.loadWindow(
                CategoryNameSetController.class.getResource("category-name-change-view.fxml"),
                300, 150, false,
                "New Category"
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("New Category", (Stage) menuBar.getScene().getWindow());
        }
    }

    @FXML
    private void menuNewIssue() {
        class newIssueSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                stage.setTitle("New Issue");

                IssueController controller = fxmlLoader.getController();
                controller.setIsIssueBeingCreated(true);
            }
        }

        try {
            WindowHelper.loadWindow(
                IssueController.class.getResource("issue-view.fxml"),
                400, 400, true,
                new newIssueSetup()
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("New Issue", (Stage) menuBar.getScene().getWindow());
        }
    }

    @FXML
    private void menuCloseBoard() {
        setCurrentBoard(null);
    }

    @FXML
    private void openAboutWindow() {
        try {
            WindowHelper.loadWindow(
                KanbanController.class.getResource("about-view.fxml"),
                325, 175, false,
                "About Kanban"
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("About", (Stage) menuBar.getScene().getWindow());
        }
    }

    private void setBoardToDefault(Stage stage) {
        isBoardLoaded.setValue(false);
        stage.setTitle("Kanban Project");

        welcomeLabel.setText("Welcome back!");
    }

    public void setCurrentBoard(Board board) {
        final Stage stage = (Stage) menuBar.getScene().getWindow();
        this.board = board;

        kanbanColumns.getChildren().clear();

        if (board == null) {
            setBoardToDefault(stage);
        } else {
            try {
                isBoardLoaded.setValue(true);
                stage.setTitle(String.format("Kanban Project: %s", board.getName()));

                welcomeLabel.setText(String.format("%s", board.getName()));

                final ArrayList<Category> categories = board.getCategories();

                // Creates the categories one by one
                for (Category category : categories) {
                    createCategoryBox(category);
                }

                // Redirect focus away from any buttons by default
                welcomeLabel.requestFocus();
            } catch (IOException | RuntimeIOException e) {
                setBoardToDefault(stage);

                WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Could Not Load Board",
                    String.format(
                        """
                        There was an error while loading the "%s" Board.
                        Please try again later, or send a bug report through Discord.
                        """, board.getName()
                    ),
                    stage
                );
            }
        }
    }

    void createCategoryBox(Category category) throws IOException, RuntimeIOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KanbanController.class.getResource("components/category-box.fxml"));
        VBox categoryBox = fxmlLoader.load();
        CategoryBoxController controller =  fxmlLoader.getController();

        controller.setCategoryAndSortingProperty(category, sortingType);

        kanbanColumns.getChildren().add(categoryBox);
    }

    public void removeCategoryBox(int categoryId) {
        kanbanColumns.getChildren().remove(categoryId);
    }

    public void moveCategoryBox(int oldCategoryId, int newCategoryId) {
        VBox categoryBox = (VBox) kanbanColumns.getChildren().get(oldCategoryId);
        kanbanColumns.getChildren().remove(oldCategoryId);
        kanbanColumns.getChildren().add(newCategoryId, categoryBox);
    }

    public void openIssue(Issue issue) {
        class openIssueWindowSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                stage.setTitle(String.format("Issue: %s", issue.getName()));
                scene.getRoot().requestFocus();

                IssueController controller = fxmlLoader.getController();
                controller.setIsIssueBeingCreated(false);
                controller.setIssue(issue);
            }
        }

        try {
            WindowHelper.loadWindow(
                IssueController.class.getResource("issue-view.fxml"),
                700, 400, true,
                new openIssueWindowSetup()
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("Issue Detail", (Stage) menuBar.getScene().getWindow());
        }
    }

    /**
     * Returns global MenuBar
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Board getBoard() {
        return board;
    }
}
