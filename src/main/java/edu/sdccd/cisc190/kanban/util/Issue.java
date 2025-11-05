package edu.sdccd.cisc190.kanban.util;

import java.util.Date;

public class Issue {
    private String id;
    private String name;
    private String description;
    private IssueType type;

    private int category;

    private String creator;
    private String assignee;

    private Date dateCreated = new Date();
    private Date dateModified = new Date();

    Issue(
        String name,
        String description,
        String creator,
        IssueType type,
        int issueNumber
    ) {
        this.name = name;
        this.description = description;
        this.type = type;

        this.creator = creator;
        this.assignee = null;

        this.category = 0;
        this.id = String.format("#%07d", issueNumber);

        this.dateCreated.setTime(System.currentTimeMillis());
        this.dateModified.setTime(System.currentTimeMillis());
    }
}
