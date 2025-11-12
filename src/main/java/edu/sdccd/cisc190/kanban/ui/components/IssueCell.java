package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.models.IssueType;
import edu.sdccd.cisc190.kanban.ui.KanbanController;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.util.exceptions.IssueNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;

public class IssueCell extends ListCell<Issue> {
    private final VBox rootView;

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label idLabel;
    @FXML private Label tagLabel;

    @FXML private ContextMenu contextMenu;
    @FXML private MenuItem contextMenuOpen;
    @FXML private Menu contextMenuMove;

    public IssueCell() throws IOException {
        FXMLLoader loader = new FXMLLoader(IssueCell.class.getResource("issue-cell.fxml"));
        loader.setController(this);
        rootView = loader.load();
        rootView.prefWidthProperty().bind(widthProperty().subtract(20));

        setOnMouseClicked(this::onAction);
        setOnContextMenuRequested(this::contextMenuRequested);

        contextMenuOpen.setOnAction(this::contextMenuOpenAction);
    }

    @Override
    protected void updateItem(Issue issue, boolean empty) {
        final Issue originalIssue = getItem();

        super.updateItem(issue, empty);

        if (empty || issue == null) {
            setText(null);
            setGraphic(null);
        } else {
            final IssueType originalIssueType = originalIssue == null ? null : originalIssue.getType();

            this.setItem(issue);
            this.setGraphic(rootView);

            titleLabel.setText(issue.getName());
            idLabel.setText(issue.getIdString());

            // Fixes flicker when clicking and moving issues
            if (issue.getType() != originalIssueType) {
                tagLabel.setText(issue.getType().getLetter());
                tagLabel.getStyleClass().removeAll(
                    Arrays.stream(IssueType.values())
                        .map(IssueType::getTagStyle)
                        .toArray(String[]::new)
                );
                tagLabel.getStyleClass().add(issue.getType().getTagStyle());
            }

            // Checks if the string is too long and adds ellipses if it is.
            if (issue.getDescription().length() > 100) {
                descriptionLabel.setText(issue.getDescription().substring(0, 97) + "...");
            } else {
                descriptionLabel.setText(issue.getDescription());
            }
        }
    }

    private void contextMenuOpenAction(ActionEvent ignored) {
        openIssue();
    }

    private void onAction(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            openIssue();
        }
    }

    private void openIssue() {
        KanbanController controller = KanbanApplication.getController();
        controller.openIssue(getItem());
    }

    private void contextMenuRequested(ContextMenuEvent event) {
        if (itemProperty().get() == null) {
            return;
        }

        contextMenu.show(this, event.getScreenX(), event.getScreenY());
        contextMenuMove.getItems().clear();

        final Board board = KanbanApplication.getController().getBoard();
        String[] categories = board.getCategoriesNames();
        for (int i = 0; i < categories.length; i++) {
            CheckMenuItem menuItem = new CheckMenuItem(categories[i]);
            menuItem.setUserData(i);
            menuItem.setSelected(false);
            menuItem.setOnAction(this::onContextMenuMoveAction);

            final int issueId = itemProperty().get().getId();

            try {
                if (i == board.getCategoryOfIssue(issueId)) {
                    menuItem.setSelected(true);
                    menuItem.setOnAction(null);
                }
            } catch (IssueNotFoundException e) {
                throw new RuntimeException("Unexpected IssueNotFoundException", e);
            }

            contextMenuMove.getItems().add(menuItem);
        }
    }

    private void onContextMenuMoveAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();

        final Board board = KanbanApplication.getController().getBoard();
        final int issueId = itemProperty().get().getId();
        final int categoryId = (int) menuItem.getUserData();

        try {
            board.moveIssue(issueId, categoryId);
        } catch (IssueNotFoundException e) {
            throw new RuntimeException("Unexpected IssueNotFoundException", e);
        }
    }
}
