package edu.sdccd.cisc190.kanban;

import edu.sdccd.cisc190.kanban.ui.KanbanController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KanbanApplication extends Application {
    private static KanbanController CONTROLLER;
    private static final Logger logger = LoggerFactory.getLogger(KanbanApplication.class);

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

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

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Returns the Controller for the main window.
     */
    public static KanbanController getController() {
        return CONTROLLER;
    }
    private void onApplicationCloseRequest(WindowEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exitting");
        alert.setHeaderText("going so soon");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            Alert gAlert = new Alert(Alert.AlertType.INFORMATION);
            logger.info("Application closed by user.");
            gAlert.setTitle("Goodbye");
            gAlert.setHeaderText(null);
            gAlert.setContentText("Bye bye!");
            gAlert.showAndWait();

        } else {
            event.consume();
        }

    }
}
