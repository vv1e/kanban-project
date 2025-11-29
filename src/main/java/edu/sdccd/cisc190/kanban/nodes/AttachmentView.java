package edu.sdccd.cisc190.kanban.nodes;

import edu.sdccd.cisc190.kanban.util.FileHelper;
import edu.sdccd.cisc190.kanban.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AttachmentView extends VBox {
    protected final File file;

    public AttachmentView() {
        this("null");
    }

    public AttachmentView(String filePath) {
        file = new File(filePath);

        Pane fileDisplayBox = getDisplayBox();
        HBox fileLabelBox = getLabelBox();
        this.getChildren().addAll(fileLabelBox, fileDisplayBox);

        this.getStyleClass().add("attachment-view");
    }

    protected Pane getDisplayBox() {
        Image fileImage;
        try {
            fileImage = new Image(
                Objects.requireNonNull(
                    AttachmentView.class.getResource("/graphics/file.png")
                ).openStream()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageView fileImageView = new ImageView(fileImage);
        fileImageView.setFitHeight(50);
        fileImageView.setFitWidth(50);


        Label sizeLabel = new Label(
            String.format("Size: %s", FileHelper.getHumanReadableSize(file.length()))
        );

        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(fileImageView, sizeLabel);

        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setAlignment(Pos.CENTER);
        vBox.getStyleClass().add("attachment-view-display-box");
        return vBox;
    }

    protected HBox getLabelBox() {
        Label label = new Label(file.getName());
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);

        Button openButton = new Button("⬆");
        openButton.setOnAction(this::openFileAction);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, openButton);

        hBox.getStyleClass().add("attachment-view-label-box");
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    private void openFileAction(ActionEvent event) {
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            WindowHelper.createGenericErrorWindow(
                Alert.AlertType.ERROR,
                "Could Not Open File",
                String.format("Please check that the file at the following path exists:\n%s", file.getAbsolutePath()),
                (Stage) ((Node) event.getSource()).getScene().getWindow()
            );
        }
    }
}