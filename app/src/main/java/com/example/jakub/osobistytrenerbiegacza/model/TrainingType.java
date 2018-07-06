package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;

/**
 * Created by Jakub on 2015-10-22.
 */
public class TrainingType implements Serializable {
    //typ treningu
    private int type;

    //dystans do przebiegnięcia w m
    private double distance;

    //czas biegu w s
    private int time;

    //średnia prędkość biegu. s/km
    private double velocity;

    //liczba powtórzeń
    private int times;

    //przerwa pomiędzy odcinkami
    private int breakTime;

    //opis treningu
    private String description;

    public TrainingType(int type, double distance, int time, double velocity, int times, int breakTime, String description) {
        this.type = type;
        this.distance = distance;
        this.time = time;
        this.velocity = velocity;
        this.times = times;
        this.breakTime = breakTime;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TrainingType clone() {
        return new TrainingType( type,  distance,  time,  velocity,  times,  breakTime,  description);
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "type=" + type +
                ", distance=" + distance +
                ", time=" + time +
                ", velocity=" + velocity +
                ", times=" + times +
                ", breakTime=" + breakTime +
                ", description='" + description + '\'' +
                '}';
    }
}
/**
 * 1) WB1
 * 2) ZB
 * 3) odcinki x razy, y metrów, z przerwa
 * 4) ćwiczenia x min ćw
 */
