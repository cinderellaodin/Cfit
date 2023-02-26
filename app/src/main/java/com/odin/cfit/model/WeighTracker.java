package com.odin.cfit.model;

import com.google.firebase.database.Exclude;

public class WeighTracker {

    String progDate;
    double progweight;
    public String key;

    public WeighTracker() {
    }

    public WeighTracker(String progDate, double progweight) {
        this.progDate = progDate;
        this.progweight = progweight;
    }

    public String getProgDate() {
        return progDate;
    }

    public void setProgDate(String progDate) {
        this.progDate = progDate;
    }

    public double getProgweight() {
        return progweight;
    }

    public void setProgweight(double progweight) {
        this.progweight = progweight;
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
