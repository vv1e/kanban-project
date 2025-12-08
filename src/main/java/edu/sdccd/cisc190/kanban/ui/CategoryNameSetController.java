package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.models.Category;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CategoryNameSetController {
    private Category category;
    private String currentCategoryName;

    @FXML
    private TextField nameField;
    private static final Logger logger = LoggerFactory.getLogger(CategoryNameSetController.class);

    @FXML
    protected void closeWindow(ActionEvent event) {
        logger.debug("Closing Window");
        WindowHelper.closeWindow(event);
    }

    @FXML
    protected void nameCategory(Event event) {
        logger.debug("Name Category");
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();

        final KanbanController controller = KanbanApplication.getController();

        final String categoryName = nameField.getText().trim();
        final List<String> categoriesNamesList = Arrays.asList(controller.getBoard().getCategoriesNames());

        if (categoryName.isEmpty()) {
            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.WARNING,
                "Invalid Name",
                "A category's name must not be blank!",
                stage
            );
        } else if (categoriesNamesList.contains(categoryName) && !categoryName.equals(currentCategoryName)) {
            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.WARNING,
                "Invalid Name",
                "A category's name must be unique!",
                stage
            );
        } else {
            if (category == null) {
                Category category = controller.getBoard().createCategory(categoryName);
                try {
                    controller.createCategoryBox(category);
                } catch (IOException e) {
                    logger.error("Failed to load html window",e);
                    throw new RuntimeException(e);
                }
            } else {
                category.setName(categoryName);
            }

            WindowHelper.closeWindow(event);
        }
    }

    public void setCategory(Category category) {
        logger.debug("Setting Category");
        this.category = category;
        this.currentCategoryName = category.getName();
        nameField.setText(category.getName());
    }
}
