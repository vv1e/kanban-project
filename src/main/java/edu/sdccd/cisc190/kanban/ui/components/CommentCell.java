package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.models.Comment;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CommentCell extends ListCell<Comment> {
    private final VBox commentBox;
    private final Label commentAuthorLabel;
    private final Label commentDateLabel;
    private final Label commentTextLabel;

    /**
     * Creates a new ListCell which displays a comment.
     */
    public CommentCell() {
        this.commentBox = new VBox();
        commentBox.setSpacing(5);
        commentBox.getStyleClass().add("comment-box");

        HBox commentInfoBox = new HBox();
        commentInfoBox.setAlignment(Pos.CENTER_LEFT);

        commentAuthorLabel = new Label();
        commentAuthorLabel.getStyleClass().add("comment-author-label");
        commentAuthorLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(commentAuthorLabel, Priority.ALWAYS);

        commentDateLabel = new Label();
        commentDateLabel.getStyleClass().add("comment-date-label");

        commentTextLabel = new Label();
        commentTextLabel.getStyleClass().add("comment-text-label");
        commentTextLabel.setWrapText(true);

        commentInfoBox.getChildren().addAll(commentAuthorLabel, commentDateLabel);
        commentBox.getChildren().addAll(commentInfoBox, commentTextLabel);
    }

    @Override
    protected void updateItem(Comment comment, boolean empty) {
        super.updateItem(comment, empty);

        if (empty || comment == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.setItem(comment);
            setGraphic(commentBox);

            commentAuthorLabel.setText(comment.getAuthor());
            commentDateLabel.setText(comment.getCreationDate());
            commentTextLabel.setText(comment.getComment());
        }
    }
}
