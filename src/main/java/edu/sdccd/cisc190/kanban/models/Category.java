package edu.sdccd.cisc190.kanban.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Category {
    private final String name;
    private final ObservableList<Issue> issues;

    public Category(String name) {
        this.name = name;
        issues = FXCollections.observableArrayList();
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public String getName() {
        return name;
    }

    public ObservableList<Issue> getIssues() {
        return issues;
    }

    public Issue getIssue(int position) {
        return issues.get(position);
    }

    public int size() {
        return issues.size();
    }
}
