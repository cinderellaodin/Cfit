package com.odin.cfit.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Cinderella on 5/19/2018.
 */

public class UserInformation {
    public String gender, age, dob, activity, bmi, condition, dailycalories, ucaloriegoals, weightforecasts;
    public double height, weight, goalweight;


    public UserInformation() {
    }

    public UserInformation(String gender, String age, String dob, String activity, String bmi, String condition, String dailycalories, String ucaloriegoals, String weightforecasts, double height, double weight, double goalweight) {
        this.gender = gender;
        this.age = age;
        this.dob = dob;
        this.activity = activity;
        this.bmi = bmi;
        this.condition = condition;
        this.dailycalories = dailycalories;
        this.ucaloriegoals = ucaloriegoals;
        this.weightforecasts = weightforecasts;
        this.height = height;
        this.weight = weight;
        this.goalweight = goalweight;
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

    public String getUcaloriegoals() {
        return ucaloriegoals;
    }

    public void setUcaloriegoals(String ucaloriegoals) {
        this.ucaloriegoals = ucaloriegoals;
    }

    public String getWeightforecasts() {
        return weightforecasts;
    }

    public void setWeightforecasts(String weightforecasts) {
        this.weightforecasts = weightforecasts;
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



    @Exclude
        public static String calculateBMI(double weight, double height) {
            double bmi =  weight / (height *height);

        if (bmi < 18.5) {
            return "Underweight";
        }
        else if (bmi < 25) {
            return "Normal";
        }
        else if (bmi < 30) {
            return "Overweight";
        }
        else {
            return "Obese";
        }
    }

    @Exclude
    public static double CalculateRequiredCalories(double goalweight){
        return goalweight * 12;
    }

    @Exclude
    public static double CalculateWeightForecast(double weight, double goalweight){
        double weightd = weight - goalweight;
        double tocal = weightd * 3500;
        double reqCal = goalweight * 12;
        double caldiff = 3500 -reqCal;

        double weightforecast = tocal / caldiff;
        return weightforecast;

        /*
        Mock Values
        198 176  = 22
        22 * 3500 = 77000
        176 * 12 = 2112
        3500 - 2112 = 1388
        77000 / 1388 = 54.47
        55.47 days
        54.47 / 7 = 7.92 weeks
        */
    }
}