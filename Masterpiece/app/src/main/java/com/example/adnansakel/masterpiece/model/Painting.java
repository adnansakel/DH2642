package com.example.adnansakel.masterpiece.model;

import android.graphics.Bitmap;

/**
 * Created by Daniel on 02/04/2016.
 */
public class Painting {

    // Variable Definition
    private String name;
    private String imageURL;
    private Bitmap image;
    private String description;
    private int value;
    private String artist;
    private byte[] imagebytearray;

    public Painting(String name, String imageURL, Bitmap image, String description, Integer value, String artist) {
        this.name = name;
        this.imageURL = imageURL;
        this.image = image;
        this.description = description;
        this.value = value;
        this.artist = artist;
    }

    public Painting(){

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
    public void setValue(int value) {
        this.value = value;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){
        return artist;
    }

    public byte[] getImagebytearray() {
        return imagebytearray;
    }

    public void setImagebytearray(byte[] imagebytearray) {
        this.imagebytearray = imagebytearray;
    }
}
