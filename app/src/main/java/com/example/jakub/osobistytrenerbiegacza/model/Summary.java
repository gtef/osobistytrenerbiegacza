package com.example.jakub.osobistytrenerbiegacza.model;

import com.example.jakub.osobistytrenerbiegacza.utils.DateFormatter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Jakub on 2015-10-22.
 */
public class Summary implements Serializable {
    private Date date;

    private String notes;

    private int comfort;

    private double distanceCovered;

    private int trainingTime;

    private double averageVelocity;

    public Summary(Date date, String notes, int comfort, double distanceCovered, int trainingTime, double averageVelocity) {
        this.date = date;
        this.notes = notes;
        this.comfort = comfort;
        this.distanceCovered = distanceCovered;
        this.trainingTime = trainingTime;
        this.averageVelocity = averageVelocity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getComfort() {
        return comfort;
    }

    public void setComfort(int comfort) {
        this.comfort = comfort;
    }

    public double getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(double distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public double getAverageVelocity() {
        return averageVelocity;
    }

    public void setAverageVelocity(double averageVelocity) {
        this.averageVelocity = averageVelocity;
    }

    public Summary clone() {
        return new Summary( date,  notes,  comfort,  distanceCovered,  trainingTime,  averageVelocity);
    }

    @Override
    public String toString() {
        return "Summary{" +
                "date=" + date +
                ", notes='" + notes + '\'' +
                ", comfort=" + comfort +
                ", distanceCovered=" + distanceCovered +
                ", trainingTime=" + trainingTime +
                ", averageVelocity=" + averageVelocity +
                '}';
    }

    public String getTrainingSummary(List<TrainingType> parts) {
        String text = "";
        for(TrainingType part: parts){
            text +=part.getDescription()+"\n";
        }
        text +=
                "Przebiegnięty dystans: "+distanceCovered+" m"+
                "\nCałkowity czas: " + trainingTime +" s"+
                "\nŚrednia prędkość: " + DateFormatter.minPerKm(averageVelocity) +
                "\nNotatki: " + notes +
                "\nOcena: "+comfort;

        return text;
    }
/*
    Lekki bieg 60 min.
            Odcinki 3x30s, przerwa 60s
    Przebiegnięty dystans: 12km
    Całkowity czas: 80 min" +
            "\nŚrednia prędkość: 5'23\"" +
            "\nNotatki: Przyjemny bieg, pogoda dobra, lekkie podmuchy wiatru" +
            "\nOcena: 4";*/
}

