package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;

/**
 * Created by Jakub on 2016-03-07.
 */
public class GpxPoint implements Serializable{
    private double latitude;

    private double longitude;

    public GpxPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public GpxPoint clone() {
        return new GpxPoint(latitude, longitude);
    }

    @Override
    public String toString() {
        return "GpxPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
