package com.example.b00063271.safesplit.Entities;

public class Activities {
    private String activityString;
    private double activityType;

    public Activities() {
    }

    public Activities(String activityString, double activityType) {

        this.activityString = activityString;
        this.activityType = activityType;
    }

    public String getActivityString() {
        return activityString;
    }

    public void setActivityString(String activityString) {
        this.activityString = activityString;
    }

    public double getActivityType() {
        return activityType;
    }

    public void setActivityType(double activityType) {
        this.activityType = activityType;
    }
}
