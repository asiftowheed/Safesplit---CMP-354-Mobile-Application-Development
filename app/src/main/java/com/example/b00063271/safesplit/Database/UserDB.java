package com.example.b00063271.safesplit.Database;

import android.util.Log;

import com.example.b00063271.safesplit.Entities.History;
import com.example.b00063271.safesplit.Entities.User;
import com.example.b00063271.safesplit.SignInActivity;
import com.example.b00063271.safesplit.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class UserDB {
    private static final String TAG = "UserDB";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference rf = db.collection(C.COLLECTION_USERS);
    private SignInActivity sIn;
    private SignUpActivity sUp;
    public UserDB(SignInActivity context){
        this.sIn = context;
    }
    public UserDB(SignUpActivity context){
        this.sUp = context;
    }
    public void addUser(User u){
        final User user = u;
        Map<String, Object> docData = new HashMap<>();
        docData.put(C.USERS_NAME, user.getName());
        docData.put(C.USERS_MOBILE, user.getMobile());
        docData.put(C.USERS_TRANSACTIONS, user.getTransactionIds());
        docData.put(C.USERS_EMAIL, user.getEmail());
        docData.put(C.USERS_GROUPS,user.getGroupIds());
        docData.put("id",user.getID());
        Log.d(TAG, "addUser: "+user.getMobile());
        rf.document(user.getMobile()).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(user.getHistories()==null) user.setHistories(new ArrayList<History>());
                rf.document(user.getMobile()).collection(C.COLLECTION_USERS_HISTORY).document().set(user.getHistories());
            }
        });

    }
    public void deleteUser(User user){

    }
    public User getUser(String userID){
        return new User();
    }
    public void getUserListener(String userID){
    }
    public void getUserList(final String userID){
    }

}
