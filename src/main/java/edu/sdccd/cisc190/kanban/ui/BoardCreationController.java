package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.util.Board;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;

public class BoardCreationController {
    @FXML
    private TextField nameField;

    @FXML
    protected void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }

    @FXML
    protected void createBoard(Event event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        if (nameField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name!");
            alert.setContentText("A kanban board must have a name that isn't empty.");
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        } else {
            KanbanController controller = KanbanApplication.getController();
            controller.setCurrentBoard(new Board(nameField.getText()));
            stage.close();
        }
    }
}
