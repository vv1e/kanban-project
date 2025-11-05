package edu.sdccd.cisc190.kanban.ui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class KanbanController {
    @FXML
    private MenuBar menuBar;

    @FXML
    protected void initialize() {
        menuBar.setUseSystemMenuBar(System.getProperty("os.name").toLowerCase().contains("mac"));
    }
}
