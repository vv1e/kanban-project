package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.ui.components.IssueCell;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.models.IssueType;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import edu.sdccd.cisc190.kanban.util.interfaces.WindowSetup;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class KanbanController {
    private Board board;
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
        // macOS: This makes the menu bar use the system one instead of the JavaFX one.
        menuBar.setUseSystemMenuBar(System.getProperty("os.name").toLowerCase().contains("mac"));

        // Several categories are supposed to be inaccessible when there is no board loaded.
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
    private void menuNewBoard() {
        try {
            WindowHelper.loadWindow(
                BoardCreationController.class.getResource("board-creation-view.fxml"),
                300, 165, false,
                "New Board"
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("New Board");
        }
    }

    @FXML
    private void menuNewIssue() {
        class newIssueSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                stage.setTitle("New Issue");

                IssueController controller = fxmlLoader.getController();
                controller.setIsIssueBeingCreated(true);
            }
        }

        try {
            WindowHelper.loadWindow(
                IssueController.class.getResource("issue-view.fxml"),
                300, 400, true,
                new newIssueSetup()
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("New Issue");
        }
    }

    @FXML
    private void menuCloseBoard() {
        setCurrentBoard(null);
    }

    @FXML
    private void openAboutWindow() {
        try {
            WindowHelper.loadWindow(
                KanbanController.class.getResource("about-view.fxml"),
                325, 175, false,
                "About Kanban"
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("About");
        }
    }

    private void setBoardToDefault(Stage stage) {
        isBoardLoaded.setValue(false);
        stage.setTitle("Kanban Project");

        welcomeLabel.setText("Welcome back!");
    }

    public void setCurrentBoard(Board board) {
        final Stage stage = (Stage) menuBar.getScene().getWindow();
        this.board = board;

        kanbanColumns.getChildren().clear();

        if (board == null) {
            setBoardToDefault(stage);
        } else {
            try {
                isBoardLoaded.setValue(true);
                stage.setTitle(String.format("Kanban Project: %s", board.getName()));

                welcomeLabel.setText(String.format("%s", board.getName()));

                final ArrayList<StringProperty> issueCategoryNames = board.getIssueCategoryNames();
                final ArrayList<ObservableList<Issue>> issueCategories = board.getIssues();

                // Creates the categories one by one
                for (int i = 0; i < issueCategoryNames.size(); i++) {
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
                    categoryLabel.setAlignment(Pos.CENTER_LEFT);
                    categoryLabel.setMaxWidth(Double.MAX_VALUE);

                    categoryLabel.setViewOrder(0);
                    listView.setViewOrder(1);

                    categoryContainer.getStyleClass().add("common-column");
                    categoryLabelContainer.getStyleClass().add("kanban-column-label-container");
                    categoryLabel.getStyleClass().add("kanban-column-label");
                    listView.getStyleClass().add("common-column-list");

                    listView.setCellFactory((issue -> {
                        try {
                            return new IssueCell();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));

                    categoryLabelContainer.getChildren().add(categoryLabel);
                    categoryContainer.getChildren().addAll(categoryLabelContainer, listView);

                    kanbanColumns.getChildren().add(categoryContainer);
                }

                // PLACEHOLDER: You cannot add issues right now, this for testing.
                board.createIssue("Lorem Ipsum", "Lorem Ipsum Dolor Sit Amet, Consectetur Adipiscing Elit", IssueType.BUG_REPORT, "testUser");
                board.createIssue("Ipsum Lorem", "Elit Adipiscing Consectetur, Amet Sit Dolor Ipsum Lorem", IssueType.FEATURE_REQUEST, "testUser");
            } catch (Exception e) {
                setBoardToDefault(stage);

                WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Could Not Load Board",
                    String.format(
                        """
                        There was an error while loading the "%s" Board.
                        Please try again later, or send a bug report through Discord.
                        """, board.getName()
                    )
                );
            }
        }
    }

    public void openIssue(Issue issue) {
        class openIssueWindowSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                stage.setTitle(String.format("Issue: %s", issue.getName()));
                scene.getRoot().requestFocus();

                IssueController controller = fxmlLoader.getController();
                controller.setIsIssueBeingCreated(false);
                controller.setIssue(issue);
            }
        }

        try {
            WindowHelper.loadWindow(
                IssueController.class.getResource("issue-view.fxml"),
                600, 400, true,
                new openIssueWindowSetup()
            );
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("Issue Detail");
        }
    }

    /**
     * Returns global MenuBar
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Board getBoard() {
        return board;
    }
}
