package com.odin.cfit.model;

public class UserReqCalorie {


    public String requiredCal, weightDiff;

    public UserReqCalorie() {
    }

    public UserReqCalorie(String requiredCal, String weightDiff) {
        this.requiredCal = requiredCal;
        this.weightDiff = weightDiff;
    }

    public String getRequiredCal() {
        return requiredCal;
    }

    public void setRequiredCal(String requiredCal) {
        this.requiredCal = requiredCal;
    }

    public String getWeightDiff() {
        return weightDiff;
    }

    public void setWeightDiff(String weightDiff) {
        this.weightDiff = weightDiff;
    }
}
