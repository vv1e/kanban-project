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
import javafx.geometry.Pos;
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

    @FXML private MenuItem menuFileNewCategory;
    @FXML private MenuItem menuFileNewIssue;
    @FXML private MenuItem menuFileSave;
    @FXML private MenuItem menuFileSaveAs;
    @FXML private MenuItem menuFileClose;

    @FXML
    protected void initialize() {
        menuBar.setUseSystemMenuBar(System.getProperty("os.name").toLowerCase().contains("mac"));

        menuFileNewCategory.disableProperty().bind(isBoardLoaded.not());
        menuFileNewIssue.disableProperty().bind(isBoardLoaded.not());
        menuFileSave.disableProperty().bind(isBoardLoaded.not());
        menuFileSaveAs.disableProperty().bind(isBoardLoaded.not());
        menuFileClose.disableProperty().bind(isBoardLoaded.not());

        openLabel.visibleProperty().bind(isBoardLoaded.not());
        openLabel.managedProperty().bind(isBoardLoaded.not());

        kanbanColumns.visibleProperty().bind(isBoardLoaded);
        kanbanColumns.managedProperty().bind(isBoardLoaded);
    }

    @FXML
    private void newBoard() throws IOException {
        Stage currentStage = (Stage) menuBar.getScene().getWindow();

        menuBar.setDisable(true);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(true);
        }

        Stage stage = new Stage();
        stage.initOwner(currentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Board");
        stage.setResizable(false);
        stage.setWidth(300);
        stage.setHeight(165);

        FXMLLoader fxmlLoader = new FXMLLoader(BoardCreationController.class.getResource("board-creation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        menuBar.setDisable(false);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(false);
        }
    }

    @FXML
    private void closeBoard() {
        setCurrentBoard(null);
    }

    @FXML
    private void openAboutWindow() throws IOException {
        Stage currentStage = (Stage) menuBar.getScene().getWindow();

        menuBar.setDisable(true);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(true);
        }

        Stage stage = new Stage();
        stage.initOwner(currentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About Kanban");
        stage.setResizable(false);
        stage.setWidth(325);
        stage.setHeight(175);

        FXMLLoader fxmlLoader = new FXMLLoader(KanbanController.class.getResource("about-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        menuBar.setDisable(false);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(false);
        }
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

            welcomeLabel.setText(String.format("%s", board.getName()));

            final ArrayList<StringProperty> issueCategoryNames = board.getIssueCategoryNames();
            final ArrayList<ObservableList<Issue>> issueCategories = board.getIssues();

            for (int i = 0 ; i < issueCategoryNames.size(); i++) {
                VBox categoryContainer = new VBox();
                VBox categoryLabelContainer = new VBox();
                Label categoryLabel = new Label(issueCategoryNames.get(i).get().toUpperCase());
                ListView<Issue> listView = new ListView<>(issueCategories.get(i));

                VBox.setVgrow(categoryContainer, Priority.ALWAYS);
                HBox.setHgrow(categoryContainer, Priority.ALWAYS);
                HBox.setHgrow(categoryLabel, Priority.ALWAYS);
                VBox.setVgrow(listView, Priority.ALWAYS);
                categoryContainer.setMinWidth(200.0d);
                categoryLabel.setMinWidth(200.0d);
                categoryLabel.setAlignment(Pos.CENTER);
                categoryLabel.setMaxWidth(Double.MAX_VALUE);

                categoryLabel.setViewOrder(0);
                listView.setViewOrder(1);

                categoryContainer.getStyleClass().add("kanban-column");
                categoryLabelContainer.getStyleClass().add("kanban-column-label-container");
                categoryLabel.getStyleClass().add("kanban-column-label");
                listView.getStyleClass().add("kanban-column-list");

                listView.setCellFactory((issue -> new IssueCell()));

                categoryLabelContainer.getChildren().add(categoryLabel);
                categoryContainer.getChildren().addAll(categoryLabelContainer, listView);

                kanbanColumns.getChildren().add(categoryContainer);
            }

            board.createIssue("Lorem Ipsum", "Lorem Ipsum Dolor Sit Amet, Consectetur Adipiscing Elit", IssueType.BUG_REPORT, "a");
            board.createIssue("Lorem Ipsum", "Lorem Ipsum Dolor Sit Amet, Consectetur Adipiscing Elit", IssueType.FEATURE_REQUEST, "a");
        }
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }
}
