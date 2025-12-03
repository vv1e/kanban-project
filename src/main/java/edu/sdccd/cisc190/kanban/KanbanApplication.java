package edu.sdccd.cisc190.kanban;

import edu.sdccd.cisc190.kanban.ui.KanbanController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KanbanApplication extends Application {
    private static KanbanController CONTROLLER;
    private static final Logger logger = LoggerFactory.getLogger(KanbanApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Kanban Application starting.");
        FXMLLoader fxmlLoader = new FXMLLoader(KanbanController.class.getResource("kanban-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Kanban Project");
        stage.setScene(scene);
        stage.show();

        CONTROLLER = fxmlLoader.getController();

        logger.info("Kanban application started successfully.");
    }

    /**
     * Returns the Controller for the main window.
     */
    public static KanbanController getController() {
        return CONTROLLER;
    }
}
