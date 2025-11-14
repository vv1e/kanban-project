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

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class WindowHelper {
    final static MenuBar menuBar = KanbanApplication.getController().getMenuBar();

    public static void loadWindow(
        URL filePath,
        int width,
        int height,
        boolean resizable,
        String title
    ) throws IOException {
        class loadWindowGenericSetup implements WindowSetup {
            @Override
            public void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene) {
                stage.setTitle(title);
            }
        }

        loadWindow(filePath, width, height, resizable, new loadWindowGenericSetup());
    }

    public static void loadWindow(
        URL filePath,
        int width,
        int height,
        boolean resizable,
        WindowSetup windowSetup
    ) throws IOException {
        Stage currentStage = (Stage) menuBar.getScene().getWindow();

        menuBar.setDisable(true);
        for (Menu menu : menuBar.getMenus()) {
            menu.setDisable(true);
        }

        Stage stage = new Stage();
        stage.initOwner(currentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(resizable);

        FXMLLoader fxmlLoader = new FXMLLoader(filePath);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        windowSetup.setup(fxmlLoader, stage, scene);

        stage.showAndWait();

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

    public static void closeWindow(Event event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}
