package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.models.Comment;
import edu.sdccd.cisc190.kanban.util.DateFormatHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CommentCell extends ListCell<Comment> {
    private final VBox rootView;

    @FXML Label commentAuthorLabel;
    @FXML private Label commentDateLabel;
    @FXML private Label commentTextLabel;

    /**
     * Creates a new ListCell which displays a comment.
     */
    public CommentCell() throws IOException {
        FXMLLoader loader = new FXMLLoader(CommentCell.class.getResource("comment-cell.fxml"));
        loader.setController(this);
        rootView = loader.load();
    }

    @Override
    protected void updateItem(Comment comment, boolean empty) {
        super.updateItem(comment, empty);

        if (empty || comment == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.setItem(comment);
            setGraphic(rootView);

            commentAuthorLabel.setText(comment.getAuthor());
            commentDateLabel.setText(DateFormatHelper.formatDate(comment.getCreationDate()));
            commentTextLabel.setText(comment.getComment());
        }
    }
}
