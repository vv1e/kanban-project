package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.enums.IssueType;
import edu.sdccd.cisc190.kanban.ui.components.CommentCell;
import edu.sdccd.cisc190.kanban.models.Comment;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.util.ObjectHelper;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import edu.sdccd.cisc190.kanban.util.exceptions.IssueNotFoundException;
import edu.sdccd.cisc190.kanban.util.exceptions.RuntimeIOException;
import edu.sdccd.cisc190.kanban.util.interfaces.WindowSetup;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class IssueController {
    protected Issue issue;
    @FXML private VBox rootBox;

    @FXML private HBox propertiesBox;
    @FXML private VBox commentsBox;
    @FXML private VBox toggleBox;
    @FXML private HBox createButtonBox;
    @FXML private HBox issueAssigneeBox;

    @FXML private ToggleGroup typeToggleGroup;

    @FXML private TextField issueTitleField;
    @FXML private TextField issueAuthorField;
    @FXML private TextArea issueDescriptionArea;

    @FXML private Label issueDateCreatedLabel;
    @FXML private Label issueDateModifiedLabel;

    @FXML private ComboBox<String> issueCategoryComboBox;

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
        try {
            commentList.setCellFactory(comment -> {
                try {
                    return new CommentCell();
                } catch (IOException e) {
                    throw new RuntimeIOException(e);
                }
            });
        } catch (RuntimeIOException e) {
            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.ERROR,
                "Error While Opening Issue Window",
                """
                    There was an error while opening the issue window.
                    Please try again later or report the bug to us over Discord.
                    """,
                (Stage) rootBox.getScene().getWindow()
            );
        }
    }

    /**
     * Sets whether the window is a "New Issue" window or a "View Issue" window
     * @param issueBeingCreated true = New Issue, false = View Issue
     */
    public void setIsIssueBeingCreated(boolean issueBeingCreated) {
        if (issueBeingCreated) {
            ObjectHelper.removeNodes(
                propertiesBox,
                commentsBox,
                issueDateCreatedLabel,
                issueDateModifiedLabel,
                issueAuthorByLabel,
                issueAssigneeBox,
                issueCategoryComboBox
            );
            rootBox.getStyleClass().clear();
            issueTitleField.getStyleClass().remove("issue-title");
        } else {
            // Sets prompts to read-only, removes the "OK Cancel" bar, removes the labels above the prompts during View mode.
            ObjectHelper.removeNodes(
                toggleBox,
                issueTitleLabel,
                issueAuthorLabel,
                issueDescriptionLabel
            );
            ObjectHelper.setControlToReadonly(
                issueTitleField,
                issueDescriptionArea,
                issueAuthorField
            );

            // This effectively disables the "OK" and "Cancel" buttons, but still makes it possible to
            // Close the "View Issue" window with the Escape key.
            createButtonBox.setOpacity(0.0);
            createButtonBox.setManaged(false);

            rootBox.requestFocus();
        }
    }

    /**
     * Sets issue informartion in the "View Issue" window
     * @param issue Issue to be viewed
     */
    public void setIssue(Issue issue) {
        final Board board = KanbanApplication.getController().getBoard();

        this.issue = issue;

        issueTitleField.setText(issue.getName());
        issueAuthorField.setText(issue.getCreator());
        issueDateCreatedLabel.setText(
            String.format("Created: %s", issue.getDateCreatedString())
        );
        issueDateModifiedLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("Modified: %s", issue.getDateModifiedString()),
                issue.dateModifiedProperty()
            )
        );
        issueDescriptionArea.setText(issue.getDescription());
        tagLabel.setText(issue.getType().getLetter());
        tagLabel.getStyleClass().add(issue.getType().getTagStyle());
        idLabel.setText(issue.getIdString());
        issueAssigneeLabel.setText(String.format("Assignee: %s", issue.getAssignee()));

        commentList.setItems(issue.getComments());

        final String[] categoriesNames = board.getCategoriesNames();
        issueCategoryComboBox.getItems().addAll(categoriesNames);

        try {
            issueCategoryComboBox.setValue(categoriesNames[board.getCategoryOfIssue(issue.getId())]);
        } catch (IssueNotFoundException e) {
            throw new RuntimeException("Unexpected IssueNotFoundException", e);
        }
    }

    @FXML
    private void createIssue(ActionEvent event) {
        final Board board = KanbanApplication.getController().getBoard();

        String[] issueProblems = new String[10];
        int problemNumber = 0;
        IssueType type = null;

        if (issueTitleField.getText().trim().isEmpty()) {
            issueProblems[problemNumber++] = " - The issue title cannot be empty.";
        }

        if (issueDescriptionArea.getText().trim().isEmpty()) {
            issueProblems[problemNumber++] = " - The issue description cannot be empty.";
        }

        if (typeToggleGroup.getSelectedToggle() == null) {
            issueProblems[problemNumber++] = " - The issue type must be specified.";
        } else {
            type = IssueType.valueOf((String) typeToggleGroup.getSelectedToggle().getUserData());
        }

        if (issueAuthorField.getText().trim().isEmpty()) {
            issueProblems[problemNumber++] = " - The issue must have an associated author.";
        }

        if (problemNumber == 0) {
            board.createIssue(
                issueTitleField.getText().trim(),
                issueDescriptionArea.getText().trim(),
                type,
                issueAuthorField.getText().trim()
            );

            WindowHelper.closeWindow(event);
        } else {
            StringBuilder problemString = new StringBuilder();
            for (int i = 0; i < problemNumber; i++) {
                problemString.append(issueProblems[i]).append("\n");
            }

            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.ERROR,
                "Error Creating Issue",
                String.format(
                    """
                    There were one or more problems with your issue:
                    
                    %s
                    """, problemString
                ),
                (Stage) rootBox.getScene().getWindow()
            );
        }
    }

    @FXML
    private void openAssigneeDialog() throws IOException {
        class assigneeNameViewSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                AssigneeController controller = fxmlLoader.getController();
                controller.setIssue(issue);
                controller.setOnChangeCallback(IssueController.this::updateAssigneeLabel);
            }
        }

        WindowHelper.loadWindow(
            AssigneeController.class.getResource("assignee-name-view.fxml"),
            300, 150, false,
            "Change Assignee",
            rootBox.getScene().getWindow(),
            new assigneeNameViewSetup()
        );
    }

    private void updateAssigneeLabel() {
        issueAssigneeLabel.setText("Assignee: " + issue.getAssignee());
    }

    @FXML
    private void onComboBoxChangeCategory() {
        final Board board = KanbanApplication.getController().getBoard();
        final List<String> categoriesNamesList = Arrays.asList(board.getCategoriesNames());

        String categoryName = issueCategoryComboBox.getValue();
        int categoryId = categoriesNamesList.indexOf(categoryName);

        try {
            board.moveIssue(issue.getId(), categoryId);
        } catch (IssueNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void openCommentDialog() throws IOException {
        class commentViewSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                CommentController controller = fxmlLoader.getController();
                controller.setIssue(issue);
                controller.setOnChangeCallback(IssueController.this::addComment);
            }
        }

        WindowHelper.loadWindow(
                AssigneeController.class.getResource("comment-view.fxml"),
                300, 300, true,
                "Add Comment",
                rootBox.getScene().getWindow(),
                new commentViewSetup()
        );
    }

    private void addComment() {
        commentList.setItems(issue.getComments());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        WindowHelper.closeWindow(event);
    }
}
