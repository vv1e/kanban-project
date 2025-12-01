package edu.sdccd.cisc190.kanban.models;

import java.time.LocalDateTime;

public class Comment {
    private String author;
    private String comment;
    private LocalDateTime creationDate;

    public Comment() {
        // Needed by Jackson
    }

    public Comment(String author, String comment) {
        this.author = author;
        this.comment = comment;
        this.creationDate = LocalDateTime.now();
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
}
