package edu.sdccd.cisc190.kanban.models;

import edu.sdccd.cisc190.kanban.util.DateFormatHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Issue {
    private final int id;
    private final String name;
    private final String description;
    private final IssueType type;

    private final String creator;
    private String assignee;

    private final LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    private final ObservableList<Comment> comments;

    Issue(
        String name,
        String description,
        String creator,
        IssueType type,
        int id
    ) {
        this.name = name;
        this.description = description;
        this.type = type;

        this.creator = creator;
        this.assignee = "None";

        this.id = id;

        this.dateCreated = LocalDateTime.now();
        this.dateModified = LocalDateTime.now();

        this.comments = FXCollections.observableArrayList();
        this.addComment("System", "Issue created.");
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return String.format("#%06d", id+1);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public IssueType getType() {
        return type;
    }

    public String getTypeLetter() {
        return switch (type) {
            case BUG_REPORT -> "B";
            case FEATURE_REQUEST -> "F";
        };
    }

    public String getTypeStyle() {
        return switch (type) {
            case BUG_REPORT -> "bug-tag";
            case FEATURE_REQUEST -> "feature-tag";
        };
    }

    public String getCreator() {
        return creator;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
        dateModified = LocalDateTime.now();
    }

    public ObservableList<Comment> getComments() {
        return comments;
    }

    public void addComment(String author, String comment) {
        this.comments.add(new Comment(author, comment));
        dateModified = LocalDateTime.now();
    }

    public String getDateCreated() {
        return DateFormatHelper.formatDate(dateCreated);
    }

    public String getDateModified() {
        return DateFormatHelper.formatDate(dateModified);
    }
}
