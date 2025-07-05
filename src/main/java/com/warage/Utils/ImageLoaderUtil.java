package com.warage.Utils;

import javafx.scene.image.Image;
import java.net.URL;

public class ImageLoaderUtil {
    public static Image loadImage(String resourcePath) {
        URL url = ImageLoaderUtil.class.getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Image not found: " + resourcePath);
        }
        return new Image(url.toExternalForm());
    }
}
