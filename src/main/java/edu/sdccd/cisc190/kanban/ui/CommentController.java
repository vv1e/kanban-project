package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CommentController {
    @FXML TextField authorField;
    @FXML TextArea commentArea;
    @FXML private Issue issue;

    private Runnable onChangeCallback;

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }

    @FXML
    protected void closeWindow(ActionEvent event) {
        WindowHelper.closeWindow(event);
    }

    @FXML
    private void addComment(ActionEvent event) {
        if (issue == null) return;

        if (!commentArea.getText().trim().isEmpty() && !authorField.getText().trim().isEmpty()) {
            if (authorField.getText().trim().equalsIgnoreCase("System")) {
                WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Could Not Add Comment",
                    "You cannot add a comment under the name \"System\"!",
                    (Stage) commentArea.getScene().getWindow()
                );
                return;
            } else {
                issue.addComment(authorField.getText(), commentArea.getText());
            }
        } else {
            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.ERROR,
                "Could Not Add Comment",
                "A comment must have both an author and a description!",
                (Stage) commentArea.getScene().getWindow()
            );
            return;
        }
        if (onChangeCallback != null) {
            onChangeCallback.run();  // Update UI in main window
        }
        WindowHelper.closeWindow(event);
    }
}
