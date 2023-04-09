package dk.easv;

import javafx.scene.image.Image;

import java.util.List;

public class Slideshow {
    private List<Image> images;

    public Slideshow(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }

    public void addImages(Image image){
        images.add(image);
    }
}