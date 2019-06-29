package com.example.b00063271.safesplit.Database;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TransactionDB {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(C.COLLECTION_TRANSACTION);

    private double amount;
    private ArrayList<Double> moneyOweTransactions;
    private ArrayList<Double> moneyOwedTransactions;
    private ArrayList<Double> totalBalanceTransactions;
    
    private final String TAG = "TransactionDBClass";

    private OnDatabaseInteractionListener mListener;

    public TransactionDB(OnDatabaseInteractionListener mListener){
        moneyOwedTransactions = new ArrayList<>();
        moneyOweTransactions = new ArrayList<>();
        totalBalanceTransactions = new ArrayList<>();
        this.mListener = mListener;
    }

    public void getTransactions(String userMobile){
        amount = 0;
        rf_t.whereEqualTo(C.TRANSACTION_TO_ID,userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        moneyOweTransactions.clear();
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = C.round(doc.getDouble(C.TRANSACTION_AMOUNT));
                            moneyOweTransactions.add(amount);
                            totalBalanceTransactions.add(amount);
                        }
                        if(mListener!=null)
                            mListener.onDatabaseInteration(C.CALLBACK_GET_TRANSACTIONS,moneyOwedTransactions,moneyOweTransactions,totalBalanceTransactions);
                        else Log.d(TAG, "onEvent: OweCallback");
                    }
                });
        rf_t.whereEqualTo(C.TRANSACTION_FROM_ID,userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        moneyOwedTransactions.clear();
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = C.round(doc.getDouble(C.TRANSACTION_AMOUNT));
                            moneyOwedTransactions.add(amount);
                            totalBalanceTransactions.add(amount);
                        }
                        if(mListener!=null)
                            mListener.onDatabaseInteration(C.CALLBACK_GET_TRANSACTIONS,moneyOwedTransactions,moneyOweTransactions,totalBalanceTransactions);
                        else Log.d(TAG, "onEvent: OwedCallback");
                    }});
    }

    public interface OnDatabaseInteractionListener {
        void onDatabaseInteration(int requestCode, ArrayList<Double> a,ArrayList<Double> b,ArrayList<Double> c);
    }
}
