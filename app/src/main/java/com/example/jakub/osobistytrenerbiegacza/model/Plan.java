package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Jakub on 2015-10-22.
 */
public class Plan implements Serializable {
    private int actualTrainingNumber;

    private int numberOfTrainings;

    private double targetDistance;

    private boolean isPlanDone;

    private List<Training> trainings;

    private Date startDate; //kiedy plan się zaczyna

    public Plan(int actualTrainingNumber, int numberOfTrainings, double targetDistance, boolean isPlanDone, List<Training> trainings, Date startDate) {
        this.actualTrainingNumber = actualTrainingNumber;
        this.numberOfTrainings = numberOfTrainings;
        this.targetDistance = targetDistance;
        this.isPlanDone = isPlanDone;
        this.trainings = trainings;
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getActualTrainingNumber() {
        return actualTrainingNumber;
    }

    public void setActualTrainingNumber(int actualTrainingNumber) {
        this.actualTrainingNumber = actualTrainingNumber;
    }

    public int getNumberOfTrainings() {
        return numberOfTrainings;
    }

    public void setNumberOfTrainings(int numberOfTrainings) {
        this.numberOfTrainings = numberOfTrainings;
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(double targetDistance) {
        this.targetDistance = targetDistance;
    }

    public boolean isPlanDone() {
        return isPlanDone;
    }

    public void setPlanDone(boolean isPlanDone) {
        this.isPlanDone = isPlanDone;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    /**
     *
     * @param trainingNumber number of training starting from 1
     * @return
     */
    public Training getTraining(int trainingNumber) {
        return trainings.get(trainingNumber-1);
    }

    @Override
    public String toString() {
        return "Plan{" +
                "actualTrainingNumber=" + actualTrainingNumber +
                ", numberOfTrainings=" + numberOfTrainings +
                ", targetDistance=" + targetDistance +
                ", isPlanDone=" + isPlanDone +
                ", trainings=" + trainings +
                '}';
    }
}
