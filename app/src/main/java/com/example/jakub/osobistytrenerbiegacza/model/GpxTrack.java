package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jakub on 2016-03-07.
 */
public class GpxTrack implements Cloneable, Serializable {
    //Name is name of trainingType
    private String name;

    private List<GpxSegment> segments;

    public GpxTrack(String name, List<GpxSegment> segments) {
        this.name = name;
        this.segments = segments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GpxSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<GpxSegment> segments) {
        this.segments = segments;
    }

    public GpxTrack clone() {
        List<GpxSegment> csegments = new LinkedList<>();
        for(GpxSegment gs: segments) {
            csegments.add(gs.clone());
        }
        return new GpxTrack(name, csegments);
    }

    @Override
    public String toString() {
        return "GpxTrack{" +
                "name='" + name + '\'' +
                ", segments=" + segments +
                '}';
    }
}
