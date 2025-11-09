package edu.sdccd.cisc190.kanban.util;

import java.util.ArrayList;
import java.util.Date;

public class Issue {
    private final int id;
    private final String name;
    private final String description;
    private final IssueType type;

    private final String creator;
    private String assignee;

    private final Date dateCreated = new Date();
    private final Date dateModified = new Date();

    private final ArrayList<String> comments;

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
        this.assignee = null;

        this.id = id;

        this.dateCreated.setTime(System.currentTimeMillis());
        this.dateModified.setTime(System.currentTimeMillis());

        this.comments = new ArrayList<>();
    }

    public int getId() {
        return id;
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

    public String getCreator() {
        return creator;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
        dateModified.setTime(System.currentTimeMillis());
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void addComment(String comment) {
        this.comments.add(comment);
        dateModified.setTime(System.currentTimeMillis());
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }
}
