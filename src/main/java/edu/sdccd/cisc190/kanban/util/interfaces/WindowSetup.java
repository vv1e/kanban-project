package edu.sdccd.cisc190.kanban.util.interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public interface WindowSetup {
    void setup(FXMLLoader fxmlLoader, Stage stage, Scene scene);
}
