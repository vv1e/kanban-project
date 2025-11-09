package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.ui.components.IssueCell;
import edu.sdccd.cisc190.kanban.util.Board;
import edu.sdccd.cisc190.kanban.util.Issue;
import edu.sdccd.cisc190.kanban.util.IssueType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class KanbanController {
    private Board currentBoard;
    private final BooleanProperty isBoardLoaded = new SimpleBooleanProperty(false);

    @FXML private MenuBar menuBar;
    @FXML private Label welcomeLabel;
    @FXML private HBox kanbanColumns;
    @FXML private Label openLabel;

    @FXML private MenuItem menuNewCategory;
    @FXML private MenuItem menuNewIssue;
    @FXML private MenuItem menuOpen;
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuSaveAs;
    @FXML private MenuItem menuClose;

    @FXML
    protected void initialize() {
        menuBar.setUseSystemMenuBar(System.getProperty("os.name").toLowerCase().contains("mac"));

        menuNewCategory.disableProperty().bind(isBoardLoaded.not());
        menuNewIssue.disableProperty().bind(isBoardLoaded.not());
        menuOpen.disableProperty().bind(isBoardLoaded.not());
        menuSave.disableProperty().bind(isBoardLoaded.not());
        menuSaveAs.disableProperty().bind(isBoardLoaded.not());
        menuClose.disableProperty().bind(isBoardLoaded.not());

        openLabel.visibleProperty().bind(isBoardLoaded.not());
        openLabel.managedProperty().bind(isBoardLoaded.not());
    }

    @FXML
    private void newBoard() throws IOException {
        Stage currentStage = (Stage) menuBar.getScene().getWindow();

        Stage stage = new Stage();
        stage.initOwner(currentStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("New Board");
        stage.setResizable(false);
        stage.setWidth(300);
        stage.setHeight(150);

        FXMLLoader fxmlLoader = new FXMLLoader(BoardCreationController.class.getResource("board-creation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void closeBoard() {
        setCurrentBoard(null);
    }

    public void setCurrentBoard(Board board) {
        final Stage stage = (Stage) menuBar.getScene().getWindow();

        this.currentBoard = board;
        kanbanColumns.getChildren().clear();

        if (board == null) {
            isBoardLoaded.setValue(false);
            stage.setTitle("Kanban Project");

            welcomeLabel.setText("Welcome back!");
        } else {
            isBoardLoaded.setValue(true);
            stage.setTitle(String.format("Kanban Project: %s", board.getName()));

            welcomeLabel.setText(String.format("Current Board: %s", board.getName()));

            final ArrayList<StringProperty> issueCategoryNames = board.getIssueCategoryNames();
            final ArrayList<ObservableList<Issue>> issueCategories = board.getIssues();

            for (int i = 0 ; i < issueCategoryNames.size(); i++) {
                VBox categoryContainer = new VBox();
                Label categoryLabel = new Label(issueCategoryNames.get(i).get());
                ListView<Issue> listView = new ListView<>(issueCategories.get(i));

                VBox.setVgrow(categoryContainer, Priority.ALWAYS);
                VBox.setVgrow(listView, Priority.ALWAYS);
                categoryContainer.setMinWidth(200.0d);

                categoryContainer.getStyleClass().add("kanban-column");
                categoryLabel.getStyleClass().add("kanban-column-label");
                listView.getStyleClass().add("kanban-column-list");

                listView.setCellFactory((issue -> new IssueCell()));

                categoryContainer.getChildren().addAll(categoryLabel, listView);
                kanbanColumns.getChildren().add(categoryContainer);
            }

            board.createIssue("test", "test2", IssueType.BUG_REPORT, "a");
        }
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }
}
