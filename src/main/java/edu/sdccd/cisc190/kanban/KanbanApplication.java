package edu.sdccd.cisc190.kanban;

import edu.sdccd.cisc190.kanban.ui.KanbanController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KanbanApplication extends Application {
    private static KanbanController CONTROLLER;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KanbanController.class.getResource("kanban-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Kanban Project");
        stage.setScene(scene);
        stage.show();

        CONTROLLER = fxmlLoader.getController();
    }

    /**
     * Returns the Controller for the main window.
     */
    public static KanbanController getController() {
        return CONTROLLER;
    }
}
