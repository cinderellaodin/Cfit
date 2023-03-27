package com.odin.cfit.model;

import com.google.firebase.database.Exclude;

public class WorkoutPlans {

    String EntryDate, planName, ExerciseName, key;
    double exReps, exSets, exRest, exDuration;

    public WorkoutPlans() {
    }


    public WorkoutPlans(String entryDate, String planName, String exerciseName, double exReps, double exSets, double exRest, double exDuration) {
        EntryDate = entryDate;
        this.planName = planName;
        ExerciseName = exerciseName;
        this.exReps = exReps;
        this.exSets = exSets;
        this.exRest = exRest;
        this.exDuration = exDuration;
        this.key = key;
    }


    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        ExerciseName = exerciseName;
    }

    public double getExReps() {
        return exReps;
    }

    public void setExReps(double exReps) {
        this.exReps = exReps;
    }

    public double getExSets() {
        return exSets;
    }

    public void setExSets(double exSets) {
        this.exSets = exSets;
    }

    public double getExRest() {
        return exRest;
    }

    public void setExRest(double exRest) {
        this.exRest = exRest;
    }

    public double getExDuration() {
        return exDuration;
    }

    public void setExDuration(double exDuration) {
        this.exDuration = exDuration;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


}
