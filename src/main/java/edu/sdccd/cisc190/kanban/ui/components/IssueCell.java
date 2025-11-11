package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.ui.KanbanController;
import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class IssueCell extends ListCell<Issue> {
    private final VBox rootView;

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label idLabel;
    @FXML private Label tagLabel;

    public IssueCell() throws IOException {
        FXMLLoader loader = new FXMLLoader(IssueCell.class.getResource("issue-cell.fxml"));
        loader.setController(this);
        rootView = loader.load();
        rootView.prefWidthProperty().bind(widthProperty().subtract(20));

        setOnMouseClicked(this::onAction);
    }

    @Override
    protected void updateItem(Issue issue, boolean empty) {
        super.updateItem(issue, empty);

        if (empty || issue == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.setItem(issue);
            this.setGraphic(rootView);

            titleLabel.setText(issue.getName());
            idLabel.setText(issue.getIdString());

            tagLabel.setText(issue.getTypeLetter());
            tagLabel.getStyleClass().add(issue.getTypeStyle());

            // Checks if the string is too long and adds ellipses if it is.
            if (issue.getDescription().length() > 100) {
                descriptionLabel.setText(issue.getDescription().substring(0, 97) + "...");
            } else {
                descriptionLabel.setText(issue.getDescription());
            }
        }
    }

    private void onAction(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
            KanbanController controller = KanbanApplication.getController();

            controller.openIssue(getItem());
        }
    }
}
