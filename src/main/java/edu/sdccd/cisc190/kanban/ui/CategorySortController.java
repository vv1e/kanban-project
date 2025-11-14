package edu.sdccd.cisc190.kanban.ui;

import edu.sdccd.cisc190.kanban.KanbanApplication;
import edu.sdccd.cisc190.kanban.models.Board;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class CategorySortController {
    private KanbanController kanbanController;
    private Board board;
    private ObservableList<String> categoryList;

    private int boardCategoryId = 0;
    private final IntegerProperty selectedCategoryId = new SimpleIntegerProperty(0);
    private String categoryName;

    @FXML private ListView<String> categoryListView;
    @FXML private Button moveUpButton;
    @FXML private Button moveDownButton;

    @FXML
    protected void initialize() {
        kanbanController = KanbanApplication.getController();
        board = KanbanApplication.getController().getBoard();
        categoryList = FXCollections.observableArrayList(board.getCategoriesNames());

        moveUpButton.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> selectedCategoryId.get() == 0,
                selectedCategoryId
            )
        );

        moveDownButton.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> selectedCategoryId.get() == categoryList.size()-1,
                selectedCategoryId
            )
        );

        categoryListView.setItems(categoryList);
        categoryListView.setCellFactory(categoryCell ->
            new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);

                        if (item.equals(categoryName)) {
                            setStyle("-fx-font-weight: bold");
                        } else {
                            setStyle(null);
                        }
                    }
                }
            }
        );
    }

    @FXML
    private void onMoveUpAction() {
        moveCategory(selectedCategoryId.get() > 0? selectedCategoryId.get()-1 : selectedCategoryId.get());
    }

    @FXML
    private void onMoveDownAction() {
        moveCategory(selectedCategoryId.get() < categoryList.size()-1? selectedCategoryId.get()+1 : selectedCategoryId.get());
    }

    private void moveCategory(int newIndex) {
        final String categoryName = categoryList.get(selectedCategoryId.get());
        categoryListView.getItems().remove(selectedCategoryId.get());
        categoryListView.getItems().add(newIndex, categoryName);
        selectedCategoryId.set(newIndex);
    }

    @FXML
    private void onApplyAction(ActionEvent event) {
        board.moveCategory(boardCategoryId, selectedCategoryId.get());
        kanbanController.moveCategoryBox(boardCategoryId, selectedCategoryId.get());

        WindowHelper.closeWindow(event);
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        WindowHelper.closeWindow(event);
    }

    public void setCategory(int categoryId) {
        boardCategoryId = categoryId;
        selectedCategoryId.set(categoryId);
        categoryName = categoryList.get(selectedCategoryId.get());
    }
}
