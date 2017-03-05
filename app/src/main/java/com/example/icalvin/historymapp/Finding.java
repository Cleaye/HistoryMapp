package com.example.icalvin.historymapp;

import android.media.Image;

public class Finding {
    public String name;
    public String description;
    public String imageURL;
    public Image image;
    public String period;
    public String place;

    public Finding(String name, String description, String imageURL, String period, String place) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.period = period;
        this.place = place;

        getImage();
    }

    private void getImage() {

    }
}
