package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.ui.components.CommentCell;
import edu.sdccd.cisc190.kanban.models.Comment;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IssueController {
    @FXML private VBox issueBox;

    @FXML private HBox propertiesBox;
    @FXML private VBox commentsBox;
    @FXML private HBox toggleBox;
    @FXML private HBox createButtonBox;
    @FXML private HBox issueAssigneeBox;

    @FXML private ToggleButton bugToggle;
    @FXML private ToggleButton featureToggle;

    @FXML private TextField issueTitleField;
    @FXML private TextField issueAuthorField;
    @FXML private TextArea issueDescriptionArea;

    @FXML private Label issueTitleLabel;
    @FXML private Label issueAuthorLabel;
    @FXML private Label issueDescriptionLabel;
    @FXML private Label tagLabel;
    @FXML private Label idLabel;

    @FXML private Label issueAuthorByLabel;
    @FXML private Label issueAssigneeLabel;

    @FXML private ListView<Comment> commentList;

    @FXML
    protected void initialize() {
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(bugToggle, featureToggle);

        commentList.setCellFactory(comment -> new CommentCell());
    }

    /**
     * Sets whether the window is a "New Issue" window or a "View Issue" window
     * @param issueBeingCreated true = New Issue, false = View Issue
     */
    public void setIsIssueBeingCreated(boolean issueBeingCreated) {
        if (issueBeingCreated) {
            // Disables the "B #123456" box, the comments, the assignee prompt, and miscellaneous stuff during Creation mode.
            propertiesBox.setVisible(false);
            propertiesBox.setManaged(false);

            commentsBox.setVisible(false);
            commentsBox.setManaged(false);

            issueAuthorByLabel.setVisible(false);
            issueAuthorByLabel.setManaged(false);
            issueAssigneeBox.setVisible(false);
            issueAssigneeBox.setManaged(false);

            issueBox.getStyleClass().clear();
        } else {
            // Sets prompts to read-only, removes the "OK Cancel" bar, removes the labels above the prompts during View mode.
            toggleBox.setVisible(false);
            toggleBox.setManaged(false);
            createButtonBox.setVisible(false);
            createButtonBox.setManaged(false);

            issueTitleField.setEditable(false);
            issueTitleField.setFocusTraversable(false);
            issueAuthorField.setEditable(false);
            issueAuthorField.setFocusTraversable(false);
            issueDescriptionArea.setEditable(false);
            issueDescriptionArea.setFocusTraversable(false);

            issueTitleLabel.setVisible(false);
            issueTitleLabel.setManaged(false);
            issueDescriptionLabel.setVisible(false);
            issueDescriptionLabel.setManaged(false);
            issueAuthorLabel.setVisible(false);
            issueAuthorLabel.setManaged(false);
        }
    }

    /**
     * Sets issue informartion in the "View Issue" window
     * @param issue Issue to be viewed
     */
    public void setIssue(Issue issue) {
        issueTitleField.setText(issue.getName());
        issueTitleField.getStyleClass().add("issue-title");
        issueAuthorField.setText(issue.getCreator());
        issueDescriptionArea.setText(issue.getDescription());
        tagLabel.setText(issue.getTypeLetter());
        tagLabel.getStyleClass().add(issue.getTypeStyle());
        idLabel.setText(issue.getIdString());
        issueAssigneeLabel.setText(String.format("Assignee: %s", issue.getAssignee()));

        commentList.getItems().addAll(issue.getComments());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        WindowHelper.closeWindow(event);
    }
}
