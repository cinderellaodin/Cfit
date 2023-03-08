package com.odin.cfit.model;

public class FoodDiary {
    String foodtype, fooddetails, entrydate, entrytime;


    public FoodDiary() {
    }

    public FoodDiary(String foodType, String foodDetails, String tvDate, String tvTime) {
        this.foodtype =foodType;
        this.fooddetails = foodDetails;
        this.entrydate = tvDate;
        this.entrytime = tvTime;
    }

    public String getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public String getFooddetails() {
        return fooddetails;
    }

    public void setFooddetails(String fooddetails) {
        this.fooddetails = fooddetails;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public String getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(String entrytime) {
        this.entrytime = entrytime;
    }
}
