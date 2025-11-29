package edu.sdccd.cisc190.kanban.nodes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImageAttachmentView extends AttachmentView {
    public ImageAttachmentView() {
        super();
    }

    public ImageAttachmentView(String filePath) {
        super(filePath);
    }

    @Override
    protected Pane getDisplayBox() {
        HBox hBox = new HBox();
        Image image;
        try ( InputStream stream = new FileInputStream(file) ) {
            // Attempt to load image
            image = new Image(stream);
        } catch (Exception ex) {
            try {
                // Load generic "Image" image
                image = new Image(
                    Objects.requireNonNull(
                        AttachmentView.class.getResource("/graphics/image-file.png")
                    ).openStream()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        hBox.getChildren().addAll(imageView);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }
}