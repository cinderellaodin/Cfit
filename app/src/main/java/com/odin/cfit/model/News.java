package com.odin.cfit.model;

import com.google.firebase.database.Exclude;

public class News {

    public String img;
    public String title;
    public String subtitle;
    public String date;
    public String key;

    public News() {

    }

    public News( String img, String title, String subtitle, String date) {
        this.img = img;
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
