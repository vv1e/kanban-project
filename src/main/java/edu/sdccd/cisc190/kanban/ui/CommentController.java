package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentController {
    @FXML TextField authorField;
    @FXML TextArea commentArea;
    @FXML private Issue issue;

    private Runnable onChangeCallback;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);


    public void setIssue(Issue issue) {
        this.issue = issue;
        if (issue != null) {
            logger.debug("Issue object ID {} set in controller.", issue.getId());
        }
    }
    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
        logger.trace("Change callback function set.");
    }

    @FXML
    protected void closeWindow(ActionEvent event) {
        logger.info("Closing comment window.");
        WindowHelper.closeWindow(event);
    }

    @FXML
    private void addComment(ActionEvent event) {
        if (issue == null) {
            logger.error("Issue is null.");
            return;
        }

        if (!commentArea.getText().trim().isEmpty() && !authorField.getText().trim().isEmpty()) {
            if (authorField.getText().trim().equalsIgnoreCase("System")) {
                logger.warn("Author field is empty.");
                WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Could Not Add Comment",
                    "You cannot add a comment under the name \"System\"!",
                    (Stage) commentArea.getScene().getWindow()
                );
                return;
            } else {
                issue.addComment(authorField.getText(), commentArea.getText());
                logger.info("New comment added to issue ID {} by author: {}", issue.getId(), authorField.getText());
                logger.debug("Comment Content: {}", commentArea.getText());
            }
        } else {
            logger.warn("Comment field or Author field is empty.");
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
            logger.debug("Callback function executed to update main UI list.");
        }
        WindowHelper.closeWindow(event);
    }
}
