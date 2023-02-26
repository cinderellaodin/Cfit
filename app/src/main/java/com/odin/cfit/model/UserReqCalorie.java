package com.odin.cfit.model;

public class UserReqCalorie {

    public double requiredCal, weightDiff;

    public UserReqCalorie() {
    }

    public UserReqCalorie(double required_calories, double weight_diff) {
        this.requiredCal = required_calories;
        this.weightDiff = weight_diff;
    }

    public double getRequiredCal() {
        return requiredCal;
    }

    public void setRequiredCal(double requiredCal) {
        this.requiredCal = requiredCal;
    }

    public double getWeightDiff() {
        return weightDiff;
    }

    public void setWeightDiff(double weightDiff) {
        this.weightDiff = weightDiff;
    }
}
