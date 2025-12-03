package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssigneeController {
    @FXML TextField assigneeField;
    @FXML private Issue issue;

    private Runnable onChangeCallback;

    private static final Logger logger = LoggerFactory.getLogger(AssigneeController.class);


    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
        logger.trace("Change callback function set.");
    }
    @FXML
    protected void closeWindow(ActionEvent event) {
        logger.info("Closing Assignee window.");
        WindowHelper.closeWindow(event);
    }

    @FXML
    private void changeAssignee(ActionEvent event) {
        if (issue != null && !assigneeField.getText().isBlank()) {
            issue.setAssignee(assigneeField.getText());
            logger.info("Assignee updated for issue ID {}", issue.getId());

        } else {
            assert issue != null;
            issue.setAssignee("None");
            logger.info("Assignee cleared for issue ID {}.", issue.getId());
        }
        if (onChangeCallback != null) {
            onChangeCallback.run();  // Update UI in main window
            logger.debug("Callback function executed to update the main UI.");
        }
        WindowHelper.closeWindow(event);
    }

}
