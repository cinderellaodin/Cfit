package com.odin.cfit.model;

/**
 * Created by Cinderella on 5/19/2018.
 */

public class UserInformation {
    public String gender, age, dob, activity, bmi, condition, dailycalories;
    public double height, weight, goalweight;


    public UserInformation() {
    }

    public UserInformation(String gender, String age, String dob, String activity, String bmi, String condition, String dailycalories, double height, double weight, double goalweight) {
        this.height = height;
        this.weight = weight;
        this.goalweight = goalweight;
        this.gender = gender;
        this.age = age;
        this.dob = dob;
        this.activity = activity;
        this.bmi = bmi;
        this.condition = condition;
        this.dailycalories = dailycalories;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDailycalories() {
        return dailycalories;
    }

    public void setDailycalories(String dailycalories) {
        this.dailycalories = dailycalories;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getGoalweight() {
        return goalweight;
    }

    public void setGoalweight(double goalweight) {
        this.goalweight = goalweight;
    }
}