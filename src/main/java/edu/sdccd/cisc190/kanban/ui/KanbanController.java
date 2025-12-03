package edu.sdccd.cisc190.kanban.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.enums.SortDirection;
import edu.sdccd.cisc190.kanban.enums.SortField;
import edu.sdccd.cisc190.kanban.models.Category;
import edu.sdccd.cisc190.kanban.enums.SortingType;
import edu.sdccd.cisc190.kanban.ui.components.CategoryBoxController;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.models.Issue;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import edu.sdccd.cisc190.kanban.util.exceptions.RuntimeIOException;
import edu.sdccd.cisc190.kanban.util.interfaces.WindowSetup;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KanbanController {
    private Board board;
    private final BooleanProperty isBoardLoaded = new SimpleBooleanProperty(false);
    private final ObjectProperty<SortingType> sortingType = new SimpleObjectProperty<>(SortingType.NONE);

    @FXML private MenuBar menuBar;
    @FXML private Label welcomeLabel;
    @FXML private HBox kanbanColumns;
    @FXML private Label welcomeSubtitleLabel;

    @FXML private MenuItem menuFileNewCategory;
    @FXML private MenuItem menuFileNewIssue;
    @FXML private MenuItem menuFileSave;
    @FXML private MenuItem menuFileSaveAs;
    @FXML private MenuItem menuFileClose;

    @FXML private ToggleGroup menuSortFieldToggleGroup;
    @FXML private ToggleGroup menuSortDirectionToggleGroup;

    File currentFile;

    private static final Logger logger = LoggerFactory.getLogger(KanbanController.class);

    @FXML
    protected void initialize() {
        logger.info("KanbanController initializing.");

         // macOS: This makes the menu bar use the system one instead of the JavaFX one.
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_MENU_BAR)) {
            menuBar.setUseSystemMenuBar(true);
            logger.debug("System menu bar support detected and enabled for macOS.");
        }

        // Several categories are supposed to be inaccessible when there is no board loaded.
        MenuItem[] boardDependentMenus = new MenuItem[]{
            menuFileNewCategory,
            menuFileNewIssue,
            menuFileSave,
            menuFileSaveAs,
            menuFileClose
        };

        for (MenuItem menuItem : boardDependentMenus) {
            menuItem.disableProperty().bind(isBoardLoaded.not());
        }

        welcomeSubtitleLabel.visibleProperty().bind(isBoardLoaded.not());
        welcomeSubtitleLabel.managedProperty().bind(isBoardLoaded.not());

        kanbanColumns.visibleProperty().bind(isBoardLoaded);
        kanbanColumns.managedProperty().bind(isBoardLoaded);

        menuSortFieldToggleGroup.selectedToggleProperty().addListener(this::onMenuSortModified);
        menuSortDirectionToggleGroup.selectedToggleProperty().addListener(this::onMenuSortModified);
        logger.debug("Listeners attached to sort toggle groups.");
    }

    private void onMenuSortModified(ObservableValue<? extends Toggle> ignoredObservable, Toggle ignoredOldValue, Toggle ignoredNewValue) {
        final SortField field = SortField.valueOf((String) menuSortFieldToggleGroup.getSelectedToggle().getUserData());
        final SortDirection direction = SortDirection.valueOf((String) menuSortDirectionToggleGroup.getSelectedToggle().getUserData());

        sortingType.set(SortingType.from(field, direction));
        logger.info("Sorting modified by user. Field: {}, Direction: {}", field, direction);
    }

    @FXML
    private void menuNewBoard() {
        logger.info("User selected 'New Board' from menu.");
        try {
            WindowHelper.loadWindowDisableMenuBar(
                BoardCreationController.class.getResource("board-creation-view.fxml"),
                300, 165, false,
                "New Board",
                null
            );
        } catch (IOException e) {
            logger.error("Failed to load 'New Board' window.", e);
            WindowHelper.loadErrorWindow("New Board", (Stage) menuBar.getScene().getWindow());
        }
    }

    @FXML
    private void menuNewCategory() {
        logger.info("User selected 'New Category' from menu.");
        try {
            WindowHelper.loadWindowDisableMenuBar(
                CategoryNameSetController.class.getResource("category-name-change-view.fxml"),
                300, 150, false,
                "New Category",
                null
            );
        } catch (IOException e) {
            logger.error("Failed to load 'New Category' window.", e);
            WindowHelper.loadErrorWindow("New Category", (Stage) menuBar.getScene().getWindow());
        }
    }

    @FXML
    private void menuNewIssue() {
        logger.info("User selected 'New Issue' from menu.");
        class newIssueSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                IssueController controller = fxmlLoader.getController();
                controller.setIsIssueBeingCreated(true);
            }
        }

        try {
            WindowHelper.loadWindowDisableMenuBar(
                IssueController.class.getResource("issue-view.fxml"),
                400, 500, true,
                "New Issue",
                new newIssueSetup()
            );
            logger.debug("New Issue window FXML loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load 'New Issue' window.", e);
            WindowHelper.loadErrorWindow("New Issue", (Stage) menuBar.getScene().getWindow());
        }
    }

    @FXML
    private void menuCloseBoard() {
        logger.info("User closed the current board.");
        setCurrentBoard(null);
    }

    @FXML
    void openAboutWindow() {
        logger.info("User opened the 'About' window.");
        try {
            WindowHelper.loadWindowDisableMenuBar(
                KanbanController.class.getResource("about-view.fxml"),
                325, 175, false,
                "About Kanban",
                null
            );
        } catch (IOException e) {
            logger.error("Failed to load 'About Kanban' window.", e);
            WindowHelper.loadErrorWindow("About", (Stage) menuBar.getScene().getWindow());
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
        logger.debug("Kanban columns cleared.");

        if (board == null) {
            logger.info("Board set to null. Returning to default application state.");
            setBoardToDefault(stage);
            currentFile = null;
        } else {
            try {
                isBoardLoaded.setValue(true);
                stage.setTitle(String.format("Kanban Project: %s", board.getName()));

                welcomeLabel.setText(String.format("%s", board.getName()));
                logger.info("Loaded new board: {}", board.getName());

                final ObservableList<Category> categories = board.getCategories();
                logger.debug("Found {} categories to load for this board.", categories.size());

                // Creates the categories one by one
                for (Category category : categories) {
                    createCategoryBox(category);
                }

                // Redirect focus away from any buttons by default
                welcomeLabel.requestFocus();
            } catch (IOException | RuntimeIOException e) {
                logger.error("Error loading categories for board '{}'. Reverting to default.", board.getName(), e);
                setBoardToDefault(stage);

                WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Could Not Load Board",
                    String.format(
                        """
                        There was an error while loading the "%s" Board.
                        Please try again later, or send a bug report through Discord.
                        """, board.getName()
                    ),
                    stage
                );
            }
        }
    }

    void createCategoryBox(Category category) throws IOException, RuntimeIOException {
        logger.debug("Creating UI box for category: '{}'.", category.getName());
        FXMLLoader fxmlLoader = new FXMLLoader(KanbanController.class.getResource("components/category-box.fxml"));
        VBox categoryBox = fxmlLoader.load();
        CategoryBoxController controller =  fxmlLoader.getController();

        controller.setCategoryAndSortingProperty(category, sortingType);

        kanbanColumns.getChildren().add(categoryBox);
    }

    public void removeCategoryBox(int categoryId) {
        logger.debug("Removing UI box for category: '{}'.", categoryId);
        kanbanColumns.getChildren().remove(categoryId);
    }

    public void moveCategoryBox(int oldCategoryId, int newCategoryId) {
        logger.info("Moving category box from {} to {}", oldCategoryId, newCategoryId);
        VBox categoryBox = (VBox) kanbanColumns.getChildren().get(oldCategoryId);
        kanbanColumns.getChildren().remove(oldCategoryId);
        kanbanColumns.getChildren().add(newCategoryId, categoryBox);
    }

    public void openIssue(Issue issue) {
        logger.info("Opening detail window for issue {}", issue.getName());
        class openIssueWindowSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                IssueController controller = fxmlLoader.getController();
                controller.setIsIssueBeingCreated(false);
                controller.setIssue(issue);
                logger.debug("Issue window controller configured for view mode.");
            }
        }

        try {
            WindowHelper.loadWindowDisableMenuBar(
                IssueController.class.getResource("issue-view.fxml"),
                700, 400, true,
                String.format("Issue: %s", issue.getName()),
                new openIssueWindowSetup()
            );
            logger.trace("Failed to load issue window.");
        } catch (IOException e) {
            WindowHelper.loadErrorWindow("Issue Detail", (Stage) menuBar.getScene().getWindow());
        }
    }

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);


    public void loadFromFile(File file) {
        this.currentFile = file;
        try {
            Board board = mapper.readValue(file, Board.class);
            setCurrentBoard(board);
        } catch (Exception e) {
            logger.error("Error while loading board from file '{}'.", file.getName(), e);

            WindowHelper.createGenericErrorWindow(
                    Alert.AlertType.ERROR,
                    "Could Not Load Board",
                    "Failed to load Kanban board from file.",
                    (Stage) menuBar.getScene().getWindow()
            );
        }
    }

    public void saveToFile(File file) {
        this.currentFile = file;

        try {
            mapper.writeValue(file, board); // board is your currently loaded Board
            logger.info("Board saved to '{}'.", file.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Error while saving board to '{}'.", file.getAbsolutePath(), e);
        }
    }

    private Stage getStage() {
        return (Stage) menuBar.getScene().getWindow();
    }

    @FXML
    private void handleOpen() {
        FileChooser chooser = getFileChooser();
        chooser.setTitle("Open Kanban Board");
        File file = chooser.showOpenDialog(getStage());

        if (file != null) {
            KanbanApplication.getController().loadFromFile(file);
            logger.info("Opened file: {}",  file.getAbsolutePath());
        }
    }

    @FXML
    private void handleSave() {
        KanbanController controller = KanbanApplication.getController();

        if (controller.getBoard() == null) {
            return; // no board loaded, nothing to save
        }

        if (controller.currentFile == null) {
            handleSaveAs();
        } else {
            controller.saveToFile(controller.currentFile);
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser chooser = getFileChooser();
        chooser.setTitle("Save Kanban Board As");
        chooser.setInitialFileName(KanbanApplication.getController().getBoard().getName());
        File file = chooser.showSaveDialog(getStage());

        if (file != null) {
            KanbanApplication.getController().saveToFile(file);
        }
    }

    private FileChooser getFileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON Board Definition", "*.json")
        );
        return chooser;
    }

    /**
     * Returns global MenuBar
     */
    public MenuBar getMenuBar() {
        logger.trace("getMenuBar() called.");
        return menuBar;
    }

    public Board getBoard() {
        return board;
    }
}
