package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jakub on 2015-10-22.
 */
public class Training implements Serializable {
    //numer treningu w planie
    private int trainingNumber;

    private List<TrainingType> parts;

    private Summary summary;

    //historia trasy treningu
    private List<GpxTrack> tracks;

    public Training(int trainingNumber, List<TrainingType> parts) {
        this.trainingNumber = trainingNumber;
        this.parts = parts;
        this.summary = null;
    }

    public Training(int trainingNumber, List<TrainingType> parts, Summary summary) {
        this.trainingNumber = trainingNumber;
        this.parts = parts;
        this.summary = summary;
    }

    public Training(int trainingNumber, List<TrainingType> parts, Summary summary, List<GpxTrack> tracks) {
        this.trainingNumber = trainingNumber;
        this.parts = parts;
        this.summary = summary;
        this.tracks = tracks;
    }

    public int getTrainingNumber() {
        return trainingNumber;
    }

    public void setTrainingNumber(int trainingNumber) {
        this.trainingNumber = trainingNumber;
    }

    public List<TrainingType> getParts() {
        return parts;
    }

    public void setParts(List<TrainingType> parts) {
        this.parts = parts;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<GpxTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<GpxTrack> tracks) {
        this.tracks = tracks;
    }

    public String getTrainingDescription() {
        StringBuilder s = new StringBuilder();
        for (TrainingType tt: parts) {
            s.append(tt.getDescription()+"\n");
        }
        return s.toString();
    }

    public Training clone() {//implements cloneable?
        List<TrainingType> cparts = new LinkedList<>();
        for(TrainingType tt: parts) {
            cparts.add(tt.clone());
        }
        List<GpxTrack> ctracks = new LinkedList<>();
        for(GpxTrack gt: tracks) {
            ctracks.add(gt.clone());
        }
        return new Training( trainingNumber, cparts , summary.clone(), ctracks);
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingNumber=" + trainingNumber +
                ", parts=" + parts +
                ", summary=" + summary +
                '}';
    }
}
