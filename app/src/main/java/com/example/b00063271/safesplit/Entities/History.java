package com.example.b00063271.safesplit.Entities;

import java.util.List;

public class History {
    private String id;
    private String ActivityString;
    private double Amount;
    private String Type;
    private String group;
    private List<String> toIds;

    public History(String id, String activityString, double amount, String type, String group, List<String> toIds) {
        this.id = id;
        ActivityString = activityString;
        Amount = amount;
        Type = type;
        this.group = group;
        this.toIds = toIds;
    }

    public History(String id) {

        this.id = id;
    }

    public History(String activityString, double amount, String type, String group, List<String> toIds) {
        ActivityString = activityString;
        Amount = amount;
        Type = type;
        this.group = group;
        this.toIds = toIds;
    }

    public History() {
    }

    public String getActivityString() {
        return ActivityString;
    }

    public void setActivityString(String activityString) {
        ActivityString = activityString;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getToIds() {
        return toIds;
    }

    public void setToIds(List<String> toIds) {
        this.toIds = toIds;
    }
}
