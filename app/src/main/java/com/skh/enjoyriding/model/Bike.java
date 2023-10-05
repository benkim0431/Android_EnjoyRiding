package com.skh.enjoyriding.model;

import java.io.Serializable;

public class Bike implements Serializable {
    private int bikeId;
    private double latitude;
    private double longitude;
    private String title;
    private String snippet;
    private int drawableId;

    public Bike(double latitude, double longitude, String title, String snippet, int drawableId) {
        this.bikeId = 0;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
        this.drawableId = drawableId;
    }

    public Bike(int bikeId, double latitude, double longitude, String title, String snippet, int drawableId) {
        this.bikeId = bikeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
        this.drawableId = drawableId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
