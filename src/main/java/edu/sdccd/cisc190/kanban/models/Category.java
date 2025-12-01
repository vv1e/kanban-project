package edu.sdccd.cisc190.kanban.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private final StringProperty name;

    @JsonIgnore
    private final ObservableList<Issue> issues =
            FXCollections.observableArrayList(issue ->
                    new Observable[]{ issue.dateModifiedProperty() }
            );

    public Category(String name) {
        this.name = new SimpleStringProperty(name);
    }

    @JsonCreator
    public Category(
            @JsonProperty("name") String name,
            @JsonProperty("issues") List<Issue> serializedIssues
    ) {
        this.name = new SimpleStringProperty(name);
        if (serializedIssues != null) {
            this.issues.addAll(serializedIssues);
        }
    }

    public String getName() {
        return name.get();
    }

    public void setName(String newName) {
        name.set(newName);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObservableList<Issue> getIssues() {
        return issues;
    }

    @JsonProperty("issues")
    private List<Issue> getIssuesForSerialization() {
        return new ArrayList<>(issues);
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    public void removeIssue(int index) {
        issues.remove(index);
    }

    public Issue getIssue(int index) {
        return issues.get(index);
    }

    public int size() {
        return issues.size();
    }
}
