package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jakub on 2016-03-07.
 */
public class GpxSegment implements Serializable{
    List<GpxPoint> points;

    public GpxSegment(List<GpxPoint> points) {
        this.points = points;
    }

    public List<GpxPoint> getPoints() {
        return points;
    }

    public void setPoints(List<GpxPoint> points) {
        this.points = points;
    }

    public GpxSegment clone() {
        List<GpxPoint> cPoints = new LinkedList<>();
        for(GpxPoint gp: points) {
            cPoints.add(gp.clone());
        }
        return new GpxSegment(cPoints);
    }

    @Override
    public String toString() {
        return "GpxSegment{" +
                "points=" + points +
                '}';
    }
}
