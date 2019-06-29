package com.example.b00063271.safesplit.Entities;

import java.util.List;

public class Groups {
    private String name;
    private List<String> transactionIds;
    private List<String> userIds;
    private String Id;

    public Groups() {
    }

    public Groups(String name, List<String> transactionIds, List<String> userIds) {

        this.name = name;
        this.transactionIds = transactionIds;
        this.userIds = userIds;
    }

    public Groups(String name, List<String> transactionIds, List<String> userIds, String id) {

        this.name = name;
        this.transactionIds = transactionIds;
        this.userIds = userIds;
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(List<String> transactionIds) {
        this.transactionIds = transactionIds;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
