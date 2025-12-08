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

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

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

    private ArrayList<Path> attachmentPaths;

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

        attachmentPaths = new ArrayList<>();

        this.comments = FXCollections.observableArrayList();
        this.addComment("System", "Issue created.");
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
            @JsonProperty("assignee") String assignee,
            @JsonProperty("comments") List<Comment> loadedComments
    ) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.creator = creator;
        this.assignee = assignee;
        this.id = id;

        this.dateCreated = (dateCreated != null)
                ? dateCreated
                : LocalDateTime.now();

        this.dateModified.set((dateModified != null)
                ? dateModified
                : this.dateCreated);

        // store internally as ObservableList
        this.comments = FXCollections.observableArrayList(loadedComments);
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

        addComment(
            "System",
            String.format("Set assignee to %s.", assignee)
        );
    }

    public ArrayList<Path> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(ArrayList<Path> attachmentPaths) {
       this.attachmentPaths = attachmentPaths;
    }

    public ObservableList<Comment> getComments() {
        return comments;
    }

    public void addComment(String author, String comment) {
        this.comments.add(new Comment(author, comment));
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
}
