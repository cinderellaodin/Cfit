package com.odin.cfit.model;

import com.google.firebase.database.Exclude;

public class Upload {
    double currentweight;
    double currentwaist;
    double currentchest;
    double currenthip;
    double currentthigh;
    double currentarm;
    String imageUrl;
    public String key;


    public Upload() {
    }

    public Upload(double currentweight, double currentwaist, double currentchest, double currenthip, double currentthigh, double currentarm, String imageUrl) {
        this.currentweight = currentweight;
        this.currentwaist = currentwaist;
        this.currentchest = currentchest;
        this.currenthip = currenthip;
        this.currentthigh = currentthigh;
        this.currentarm = currentarm;
        this.imageUrl = imageUrl;

    }

    public double getCurrentweight() {
        return currentweight;
    }

    public void setCurrentweight(double currentweight) {
        this.currentweight = currentweight;
    }

    public double getCurrentwaist() {
        return currentwaist;
    }

    public void setCurrentwaist(double currentwaist) {
        this.currentwaist = currentwaist;
    }

    public double getCurrentchest() {
        return currentchest;
    }

    public void setCurrentchest(double currentchest) {
        this.currentchest = currentchest;
    }

    public double getCurrenthip() {
        return currenthip;
    }

    public void setCurrenthip(double currenthip) {
        this.currenthip = currenthip;
    }

    public double getCurrentthigh() {
        return currentthigh;
    }

    public void setCurrentthigh(double currentthigh) {
        this.currentthigh = currentthigh;
    }

    public double getCurrentarm() {
        return currentarm;
    }

    public void setCurrentarm(double currentarm) {
        this.currentarm = currentarm;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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