package edu.sdccd.cisc190.kanban.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Category {
    private final StringProperty name;
    private final ObservableList<Issue> issues;

    Category(String name) {
        this.name = new SimpleStringProperty(name);
        issues = FXCollections.observableArrayList();
    }

    void addIssue(Issue issue) {
        issues.add(issue);
    }

    void removeIssue(int issuePosition) {
        issues.remove(issuePosition);
    }

    public void setName(String name) {
        this.name.setValue(name);

        for (Issue issue : issues) {
            issue.addComment(
                "System",
                String.format("Issue moved to \"%s.\"", name)
            );
        }
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }

    public ObservableList<Issue> getIssues() {
        return issues;
    }

    Issue getIssue(int position) {
        return issues.get(position);
    }

    int size() {
        return issues.size();
    }
}
