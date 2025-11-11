package edu.sdccd.cisc190.kanban.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Board {
    private final String name;
    private final ArrayList<ObservableList<Issue>> issueCategories;
    private final ArrayList<StringProperty> issueNames;
    private final ArrayList<Integer[]> categoryStore;

    public Board(String name) {
        this.name = name;
        issueCategories = new ArrayList<>();
        issueNames = new ArrayList<>();
        categoryStore = new ArrayList<>();

        issueNames.add(new SimpleStringProperty("Backlog"));
        issueNames.add(new SimpleStringProperty("In Progress"));
        issueNames.add(new SimpleStringProperty("Bug Testing"));
        issueNames.add(new SimpleStringProperty("Completed"));
        issueNames.add(new SimpleStringProperty("Won't Do"));

        for (int i = 0; i < issueNames.size(); i++) {
            issueCategories.add(FXCollections.observableArrayList());
        }
    }

    public void createIssue(String name, String description, IssueType type, String creator) {
        issueCategories.getFirst().add(new Issue(name, description, creator, type, categoryStore.size()));
        categoryStore.add(new Integer[]{0, issueCategories.getFirst().size()-1});
    }

    // Do not use - will not work as-is.
    public Issue getIssue(int id) {
        final Integer[] indeces = categoryStore.get(id);
        return issueCategories.get(indeces[0]).get(indeces[1]);
    }

    public ArrayList<ObservableList<Issue>> getIssues() {
        return issueCategories;
    }

    public ArrayList<StringProperty> getIssueCategoryNames() {
        return issueNames;
    }

    public String getName() {
        return name;
    }
}
