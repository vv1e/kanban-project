package edu.sdccd.cisc190.kanban.util;

import java.util.ArrayList;

public class Board {
    private String name;
    private ArrayList<Issue>[] issueCategories;
    private static final String[] issueNames = {
        "Backlog",
        "In Progress",
        "Review",
        "Testing",
        "Done",
        "Won't"
    };

    private int issueAmount;

    public Board(String name) {
        this.name = name;
        issueCategories = new ArrayList[6];
        issueAmount = 0;

        for (int i = 0; i < issueCategories.length; i++) {
            issueCategories[i] = new ArrayList<Issue>();
        }
    }

    public void createIssue(String name, String description, IssueType type, String creator) {
        issueCategories[0].add(new Issue(name, description, creator, type, ++issueAmount));
    }
}
