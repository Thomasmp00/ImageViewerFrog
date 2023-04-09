package dk.easv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController {
    private final List<Image> images = new ArrayList<>();
    public Label lblShowName;
    private int currentImageIndex = 0;

    private boolean ImageShow = false;

    List<Slideshow> listOfSlideShows = new ArrayList<>();

    int slideShowCounter = 0;

    int slideShowDuration = 20000;

    boolean deleteCurrentSlideShow = false;

    @FXML
    Parent root;

    @FXML
    private ImageView imageView;

    @FXML
    private void handleBtnLoadAction() {
        List<Image> imagesToSlideShow = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) ->
            {
                imagesToSlideShow.add(new Image(f.toURI().toString()));
                //images.add(new Image(f.toURI().toString()));

            });
            Slideshow slideshow = new Slideshow(imagesToSlideShow);
            listOfSlideShows.add(slideshow);
        }
    }

    public void handleStartSlideShow(ActionEvent actionEvent){
        if(listOfSlideShows.size() != 0){
            slideShowCounter = listOfSlideShows.size() - 1;

            Thread thread = new Thread(() -> {
                while(listOfSlideShows.size() != 0){

                if(slideShowCounter == -1 ) // Makes sure the slideShow list resets by acting as a counter that resets the list
                    slideShowCounter = listOfSlideShows.size() - 1;

                // Gets and sets the correct slideShow object
                Slideshow slideshow = listOfSlideShows.get(slideShowCounter);
                List<Image> imagesFromSlideShow = slideshow.getImages();


                int imageCounter = imagesFromSlideShow.size() - 1; // A counter to switch to the next slideShowImage

                long slideShowTimer = System.currentTimeMillis() + slideShowDuration; // A slideShow timer for 20 seconds in milliseconds
                while(System.currentTimeMillis() < slideShowTimer)
                try {
                    //Set the imageCounter
                    if(imageCounter == -1)
                        imageCounter = imagesFromSlideShow.size() - 1;

                    Image imageToDisplay = imagesFromSlideShow.get(imageCounter);

                    imageView.setImage(imageToDisplay);

                    String filepath = imageView.getImage().getUrl();

                    // Schedule the UI Update on the UI thread

                    Platform.runLater(() -> {
                        lblShowName.setText(filepath);
                    });

                    Thread.sleep(2000);

                    if(deleteCurrentSlideShow) {
                        deleteCurrentSlideShow = false;
                        listOfSlideShows.remove(slideShowCounter);
                        handleStartSlideShow(actionEvent);
                        return;
                    }

                    imageCounter--;
                } catch (InterruptedException e) {
                    // Handle the exception appropriately
                    e.printStackTrace();
                }
                    slideShowCounter--;
            }});
            thread.start();
        }
    }

    public void handleDeleteCurrentSlideShow(ActionEvent actionEvent) {
        if(slideShowCounter < 0){
            deleteCurrentSlideShow = true;
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
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
}