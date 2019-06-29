package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.b00063271.safesplit.Database.ActivityDB;
import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Entities.Transactions;
import com.example.b00063271.safesplit.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class TotalBalanceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "TotalBalanceFrag";

    private final String TRANSACTION_COLLECTION = "transaction";
    private final String USERS_COLLECTION = "users";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(TRANSACTION_COLLECTION);
    private CollectionReference rf_u = db.collection(USERS_COLLECTION);

    private String userMobile;
    private String userName;

    private ActivityDB activityDB;

    private OnFragmentInteractionListener mListener;

    private TabItem totalBalTabItem;
    private ListView totalBalListView;
    private TextView totalBalAmtTextView;
    private TextView totalBalPersonTextView;
    private ImageButton totalBalSettleUpButton;
    private HashMap<String,Double> balTransactions;
    private HashMap<String,Double> oweTransactions;
    private HashMap<String,String> oweTransactionsNames;
    private HashMap<String,Double> owedTransactions;
    private HashMap<String,String> owedTransactionsNames;
    private ArrayList<HashMap<String,String>> data;
    private SimpleAdapter simpleAdapter;

    public TotalBalanceFragment() {
        // Required empty public constructor
    }

    public static TotalBalanceFragment newInstance(String param1, String param2) {
        TotalBalanceFragment fragment = new TotalBalanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDB = new ActivityDB();
        balTransactions = new HashMap<>();
        oweTransactions = new HashMap<>();
        oweTransactionsNames = new HashMap<>();
        owedTransactions = new HashMap<>();
        owedTransactionsNames = new HashMap<>();
        data = new ArrayList<>();
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getBalTransactions(userMobile);
        View view =  inflater.inflate(R.layout.fragment_total_balance, container, false);
        totalBalTabItem = (TabItem) view.findViewById(R.id.total_balance_tab_item);
        totalBalListView = (ListView) view.findViewById(R.id.total_balance_list_view);
        totalBalPersonTextView = (TextView) view.findViewById(R.id.totalBalPerson);
        totalBalAmtTextView = (TextView) view.findViewById(R.id.totalBalAmt);
        totalBalSettleUpButton = (ImageButton) view.findViewById(R.id.totalBalSettleUp);
        return view;

    }

    private void getBalTransactions(String userMobile){
        getOweTransactions(userMobile);
        getOwedTransactions(userMobile);
    }

    private void getBalTransactionDetails(){
        data.clear();
        if(oweTransactions.size()==0){
            for (Map.Entry<String, Double> entry : owedTransactions.entrySet()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("person", owedTransactionsNames.get(entry.getKey()));
                map.put("personID", entry.getKey());
                map.put("amount", String.valueOf(C.round(entry.getValue())));
                data.add(map);
            }
        } else {
            for (Map.Entry<String, Double> entry : oweTransactions.entrySet()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("person", oweTransactionsNames.get(entry.getKey()));
                double owed_amount = owedTransactions.containsKey(entry.getKey()) ? owedTransactions.get(entry.getKey()) : 0;
                map.put("personID", entry.getKey());
                map.put("amount", String.valueOf(C.round(owed_amount-entry.getValue())));
                data.add(map);
            }
        }
        updateList();
    }

    private void getOweTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("toID",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        oweTransactions.clear();
                        oweTransactionsNames.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = C.round(doc.getDouble("amount"));
                            String fromID = doc.getString("fromID");
                            String from = doc.getString("from");
                            double prev_amount = oweTransactions.containsKey(fromID) ? oweTransactions.get(fromID) : 0;
                            oweTransactions.put(fromID, C.round(prev_amount + amount));
                            oweTransactionsNames.put(fromID,from);
                        }
                        getBalTransactionDetails();
                    }
                });
    }

    private void getOwedTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("fromID",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        owedTransactions.clear();
                        owedTransactionsNames.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            HashMap<String,String> map=new HashMap<>();
                            double amount = C.round(doc.getDouble("amount"));
                            String toID = doc.getString("toID");
                            String to = doc.getString("to");
                            double prev_amount = owedTransactions.containsKey(toID) ? owedTransactions.get(toID) : 0;
                            owedTransactions.put(toID, C.round(prev_amount + amount));
                            owedTransactionsNames.put(toID,to);
                        }
                        getBalTransactionDetails();
                    }
                });
    }
    private void updateList(){
        int resource = R.layout.total_bal_list;
        String[] from = {"person", "personID","amount"};
        int[] to = {R.id.totalBalPerson, R.id.totalBalPersonID,R.id.totalBalAmt};
        // create and set the adapter
        simpleAdapter=new SimpleAdapter(getActivity(),data,resource,from,to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ImageButton b = (ImageButton) v.findViewById(R.id.totalBalSettleUp);
                final String person = ((TextView) v.findViewById(R.id.totalBalPerson)).getText().toString();
                final String amt = ((TextView) v.findViewById(R.id.totalBalAmt)).getText().toString();
                final String personID = ((TextView) v.findViewById(R.id.totalBalPersonID)).getText().toString();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle("Settle Up")
                                .setMessage("Are you sure you want to create a transaction to settle "+amt+" with "+person)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        double amount = Double.valueOf(amt);
                                        if(amount>0){
                                            createTransaction(person,personID,userName,userMobile,amount);
                                            activityDB.createActivity(userMobile,"You settled your debt with "+person+" by receiving -"+amt+"- AED",C.ACTIVITY_TYPE_SETTLE_UP);
                                        } else {
                                            createTransaction(userName,userMobile,person,personID,-1*amount);
                                            activityDB.createActivity(userMobile,"You settled your debt with "+person+" by paying -"+amt+"- AED",C.ACTIVITY_TYPE_SETTLE_UP);
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                return v;
            }
        };
        totalBalListView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    private void createTransaction(String from, String fromID, String to, String toID, double amount){
        final DocumentReference df = rf_t.document();
        Transactions transaction = new Transactions(from,fromID,to,toID,amount);
        df.set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rf_u.document(userMobile).update("transactionIds", FieldValue.arrayUnion(df.getId()));
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
