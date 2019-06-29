package com.example.b00063271.safesplit.Database;

public class C {

    public static final double round(double value){ return Math.round(value * 100.0) / 100.0; }

    public static final String COLLECTION_TRANSACTION = "transaction";
    public static final String TRANSACTION_FROM_ID = "fromID";
    public static final String TRANSACTION_FROM = "from";
    public static final String TRANSACTION_TO_ID = "toID";
    public static final String TRANSACTION_TO = "to";
    public static final String TRANSACTION_AMOUNT = "amount";

    public static final String COLLECTION_USERS = "users";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_MOBILE = "mobile";
    public static final String USERS_NAME = "name";
    public static final String USERS_TRANSACTIONS = "transactionIds";
    public static final String USERS_GROUPS = "groupIds";

    public static final String COLLECTION_USERS_HISTORY = "history";
    public static final String USERS_HISTORY_ACTIVITY = "activityString";
    public static final String USERS_HISTORY_AMOUNT = "amount";
    public static final String USERS_HISTORY_TYPE = "activityType";
    public static final String USERS_HISTORY_TOS = "to";
    public static final String USERS_HISTORY_GROUP = "group";

    public static final String COLLECTION_GROUPS = "groups";
    public static final String GROUPS_NAME = "name";
    public static final String GROUPS_TRANSACTIONS = "transactionIds";
    public static final String GROUPS_USERS = "userIds";

    public static final String SETTLE_UP = "Settle Up";

    public static final int ACTIVITY_TYPE_SETTLE_UP = 1030;

    public static final int CALLBACK_GET_TRANSACTIONS = 35;
}
