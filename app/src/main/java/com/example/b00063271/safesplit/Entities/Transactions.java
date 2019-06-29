package com.example.b00063271.safesplit.Entities;

public class Transactions {
    private double Amount;
    private String from;
    private String to;
    private String fromID;
    private String toID;

    public Transactions() {
    }

    public Transactions(String from, String fromID,String to,String toID,double amount) {
        Amount = amount;
        this.from = from;
        this.to = to;
        this.fromID = fromID;
        this.toID = toID;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }
}
