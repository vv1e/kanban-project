package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardCreationController {
    @FXML
    private TextField nameField;
    private static final Logger logger = LoggerFactory.getLogger(BoardCreationController.class);

    @FXML
    protected void closeWindow(ActionEvent event) {
        logger.debug("Closing Window");
        WindowHelper.closeWindow(event);
    }

    @FXML
    protected void createBoard(Event event) {
        logger.debug("Creating Board");
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
            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.WARNING,
                "Invalid Name",
                "A Kanban Board must have a valid name!",
                stage
            );
        } else {
            KanbanController controller = KanbanApplication.getController();
            controller.setCurrentBoard(new Board(nameField.getText().trim()));
            stage.close();
        }
    }
}
