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
import java.util.Objects;

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

        /*
         * Explanation:
         * This thing checks if the Kanban Board name would be a valid file name if saved.
         * A file cannot:
         * - Be empty
         * - Contain <, >, ", /, \, |, ?, *, or Unicode control characters (like \u0000 (NUL))
         * - Be ".", "..", or "..."
         */
        if (nameField.getText().trim().isEmpty()
            || nameField.getText().trim().matches(".*[<>:\"/\\\\|?*\\p{Cc}].*")
            || nameField.getText().trim().matches("\\.{1,3}")
        ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("Invalid name!");
            alert.setHeaderText(null);
            alert.setContentText("A Kanban Board must have a valid name!");
            alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dialog-style.css")).toExternalForm());
            Toolkit.getDefaultToolkit().beep();
            alert.showAndWait();
        } else {
            KanbanController controller = KanbanApplication.getController();
            controller.setCurrentBoard(new Board(nameField.getText().trim()));
            stage.close();
        }
    }
}
