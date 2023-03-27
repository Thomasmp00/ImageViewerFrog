package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController {
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;

    private boolean ImageShow = false;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    public void handleSlideStartSlideShow(ActionEvent actionEvent) {
        if (ImageShow) {
            ImageShow = false;
        } else if (!ImageShow) {
            ImageShow = true;
        }
        Thread thread = new Thread(() -> {

            while (ImageShow) {
                try {
                    // Display the current image for 2 seconds
                    Thread.sleep(2000);
                    if (!ImageShow) {
                        return;
                    }

                    // Switch to the next image
                    currentImageIndex = (currentImageIndex + 1) % images.size();
                    imageView.setImage(images.get(currentImageIndex));

                } catch (InterruptedException e) {
                    // Handle the exception appropriately
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}