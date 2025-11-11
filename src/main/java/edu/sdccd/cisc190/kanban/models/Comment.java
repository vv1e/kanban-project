package edu.sdccd.cisc190.kanban.models;

import edu.sdccd.cisc190.kanban.util.DateFormatHelper;

import java.time.LocalDateTime;

public class Comment {
    private final String author;
    private final String comment;
    private final LocalDateTime dateCreated;

    public Comment(String author, String comment) {
        this.author = author;
        this.comment = comment;
        this.dateCreated = LocalDateTime.now();
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public String getCreationDate() {
        return DateFormatHelper.formatDate(this.dateCreated);
    }
}
