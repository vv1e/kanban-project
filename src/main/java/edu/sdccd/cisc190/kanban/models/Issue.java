package edu.sdccd.cisc190.kanban.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.sdccd.cisc190.kanban.enums.IssueType;
import edu.sdccd.cisc190.kanban.util.DateFormatHelper;
import edu.sdccd.cisc190.kanban.util.interfaces.Sortable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class Issue implements Sortable {
    private final int id;
    private final String name;
    private final String description;
    private final IssueType type;

    private final String creator;
    private String assignee;

    private final LocalDateTime dateCreated;
    private final ObjectProperty<LocalDateTime> dateModified = new SimpleObjectProperty<>();


    private final ObservableList<Comment> comments;

    public Issue(
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
        this.dateModified.set(this.dateCreated);

        this.comments = FXCollections.observableArrayList();
        addComment("System", "Issue created.");
    }

    @JsonCreator
    public Issue(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("creator") String creator,
            @JsonProperty("type") IssueType type,
            @JsonProperty("id") int id,
            @JsonProperty("dateCreated") LocalDateTime dateCreated,
            @JsonProperty("dateModified") LocalDateTime dateModified,
            @JsonProperty("comments") List<Comment> loadedComments
    ) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.creator = creator;
        this.assignee = "None";
        this.id = id;

        this.dateCreated = (dateCreated != null)
                ? dateCreated
                : LocalDateTime.now();

        this.dateModified.set((dateModified != null)
                ? dateModified
                : this.dateCreated);

        // store internally as ObservableList
        this.comments = FXCollections.observableArrayList();

        // Wrap old JSON ArrayList → ObservableList
        if (loadedComments != null) {
            this.comments.addAll(loadedComments);
        }
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public String getIdString() {
        return String.format("#%06d", id + 1);
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
        dateModified.set(LocalDateTime.now());
        addComment("System", String.format("Set assignee to %s.", assignee));
    }

    //  return ObservableList
    public ObservableList<Comment> getComments() {
        return comments;
    }

    public void addComment(String author, String comment) {
        comments.add(new Comment(author, comment));
        dateModified.set(LocalDateTime.now());
    }

    @JsonIgnore
    public long getDateCreatedNano() {
        return dateCreated.getNano();
    }

    @JsonIgnore
    public String getDateCreatedString() {
        return DateFormatHelper.formatDate(dateCreated);
    }

    @JsonIgnore
    public long getDateModifiedNano() {
        return dateModified.get().getNano();
    }

    @JsonIgnore
    public String getDateModifiedString() {
        return DateFormatHelper.formatDate(dateModified.get());
    }

    @JsonIgnore
    public ObjectProperty<LocalDateTime> dateModifiedProperty() {
        return dateModified;
    }

    public LocalDateTime getDateModified() {
        return dateModified.get();
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified.set(dateModified);
    }
}
