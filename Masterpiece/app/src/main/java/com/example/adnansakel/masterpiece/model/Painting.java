package com.example.adnansakel.masterpiece.model;

import android.graphics.Bitmap;

/**
 * Created by Daniel on 02/04/2016.
 */
public class Painting {

    // Variable Definition
    String name;
    String imageURL;
    Bitmap image;
    String description;
    Integer value;

    public Painting(String name, String imageURL, Bitmap image, String description, Integer value) {
        this.name = name;
        this.imageURL = imageURL;
        this.image = image;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getValue() {
        return value;
    }

    // Can be used to set the value of the painting in the initial setup
    public void setValue(int type) {
        this.value = type;
    }

}
