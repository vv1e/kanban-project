package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.util.Issue;
import edu.sdccd.cisc190.kanban.util.IssueType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class IssueCell extends ListCell<Issue> {
    @FXML public VBox rootView;

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label idLabel;
    @FXML private Label tagLabel;

    public IssueCell() {
        try {
            FXMLLoader loader = new FXMLLoader(IssueCell.class.getResource("issue-cell.fxml"));
            loader.setController(this);
            rootView = loader.load();
            rootView.prefWidthProperty().bind(widthProperty().subtract(20));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            idLabel.setText(String.format("#%06d", issue.getId()+1));

            tagLabel.setText(issue.getType() == IssueType.BUG_REPORT? "B": "F");
            tagLabel.getStyleClass().add(issue.getType() == IssueType.BUG_REPORT? "bug-tag": "feature-tag");

            if (issue.getDescription().length() > 100) {
                descriptionLabel.setText(issue.getDescription().substring(0, 100) + "...");
            } else {
                descriptionLabel.setText(issue.getDescription());
            }
        }
    }
}
