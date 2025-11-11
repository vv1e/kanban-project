package edu.sdccd.cisc190.kanban.ui.components;

import edu.sdccd.cisc190.kanban.models.Issue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CategoryBoxController {
    @FXML public Label categoryLabel;
    @FXML public ListView<Issue> categoryListView;

}
