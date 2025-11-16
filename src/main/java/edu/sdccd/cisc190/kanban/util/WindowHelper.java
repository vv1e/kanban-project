package edu.sdccd.cisc190.kanban.util;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.util.interfaces.WindowSetup;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class WindowHelper {
    /**
     * Loads a window using FXML. Use this only your controller is inside a window that is not the
     * main window.
     * @param filePath Path to the FXML file.
     * @param width Window width.
     * @param height Window height.
     * @param resizable Set whether the window can be resized (dialogs typically should not be)
     * @param title Window title.
     * @param initWindow The parent Window.
     * @param windowSetup Any additional setup (e.g. setting an issue inside IssueController)
     * @throws IOException Exception thrown when FXML file cannot be loaded.
     */
    public static void loadWindow(
        URL filePath,
        int width,
        int height,
        boolean resizable,
        String title,
        Window initWindow,
        WindowSetup windowSetup
    ) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(initWindow);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(resizable);
        stage.setTitle(title);

        FXMLLoader fxmlLoader = new FXMLLoader(filePath);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        if (windowSetup != null) windowSetup.setup(fxmlLoader, stage, scene);

        stage.showAndWait();
    }

    /**
     * Loads a window using FXML. Use this only if the controller is inside the main window.
     * @param filePath Path to the FXML file.
     * @param width Window width.
     * @param height Window height.
     * @param resizable Set whether the window can be resized (dialogs typically should not be)
     * @param title Window title.
     * @param windowSetup Any additional setup (e.g. setting an issue inside IssueController)
     * @throws IOException Exception thrown when FXML file cannot be loaded.
     */
    public static void loadWindowDisableMenuBar(
        URL filePath,
        int width,
        int height,
        boolean resizable,
        String title,
        WindowSetup windowSetup
    ) throws IOException {
        final MenuBar menuBar = KanbanApplication.getController().getMenuBar();

        menuBar.setDisable(true);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(true);
        }

        try {
            loadWindow(filePath, width, height, resizable, title, menuBar.getScene().getWindow(), windowSetup);
        } catch (IOException e) {
            menuBar.setDisable(false);
            for (Menu menu : menuBar.getMenus()) {
                menu.setDisable(false);
            }

            throw new IOException(e);
        }

        menuBar.setDisable(false);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(false);
        }
    }

    public static void loadErrorWindow(String windowName, Stage stage) {
        createGenericErrorWindow(
            Alert.AlertType.ERROR,
            "Error Loading Window",
            String.format(
                """
                Kanban Project could not load the "%s" window.
                Please send a bug report over Discord ASAP!""", windowName
            ),
            stage
        );
    }

    private static Alert createAlert(Alert.AlertType type, String title, String body, Stage stage) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(stage);
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(KanbanApplication.class.getResource("/dialog-style.css")).toExternalForm());
        alert.setContentText(body);

        return alert;
    }

    public static void createGenericErrorWindow(Alert.AlertType type, String title, String body, Stage stage) {
        Alert alert = createAlert(type, title, body, stage);

        Toolkit.getDefaultToolkit().beep();
        alert.showAndWait();
    }

    public static Alert createConfirmationAlert(Alert.AlertType type, String title, String body, Stage stage) {
        Alert alert = createAlert(type, title, body, stage);
        alert.getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);

        alert.showAndWait();

        return alert;
    }

    /**
     * Close caller's Window.
     * @param event Supplied event (e.g. ActionEvent)
     */
    public static void closeWindow(Event event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}
