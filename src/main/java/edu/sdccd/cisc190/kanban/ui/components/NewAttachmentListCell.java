package edu.sdccd.cisc190.kanban.ui.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.nio.file.Path;

public class NewAttachmentListCell extends ListCell<Path> {
    private final HBox rootView;

    @FXML private Label attachmentNameLabel;
    @FXML private Button attachmentRemoveButton;

    public NewAttachmentListCell() throws IOException {
        FXMLLoader loader = new FXMLLoader(NewAttachmentListCell.class.getResource("new-attachment-cell.fxml"));
        loader.setController(this);
        rootView = loader.load();
    }

    @Override
    protected void updateItem(Path path, boolean empty) {
        super.updateItem(path, empty);

        if (empty || path == null) {
            setText(null);
            setGraphic(null);

            attachmentRemoveButton.setDisable(true);
        } else {
            setItem(path);
            setGraphic(rootView);

            attachmentNameLabel.setText(path.getFileName().toString());

            attachmentRemoveButton.setDisable(false);
            attachmentRemoveButton.setOnAction(this::onRemoveButtonAction);
        }
    }

    @FXML
    private void onRemoveButtonAction(ActionEvent ignored) {
        Path pathItem = getItem();

        if (pathItem != null) {
            ListView<Path> listView = getListView();
            listView.getItems().remove(pathItem);
        }
    }
}
