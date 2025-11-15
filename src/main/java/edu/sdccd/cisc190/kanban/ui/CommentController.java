package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.scene.control.TextField;

public class CommentController {
    @FXML TextField authorField;
    @FXML TextField commentField;
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
        if (issue != null && !commentField.getText().isBlank()) {
            issue.addComment(authorField.getText(),  commentField.getText());
        } else {
            assert issue != null;
            issue.addComment("None",  "None");
        }
        if (onChangeCallback != null) {
            onChangeCallback.run();  // Update UI in main window
        }
        WindowHelper.closeWindow(event);
    }
}
