package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class MoneyOwedFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "MoneyOwedFrag";

    private ActivityDB activityDB;

    private final String TRANSACTION_COLLECTION = "transaction";
    private final String USERS_COLLECTION = "users";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_t = db.collection(TRANSACTION_COLLECTION);
    private CollectionReference rf_u = db.collection(USERS_COLLECTION);

    private String userMobile;
    private String userName;

    private OnFragmentInteractionListener mListener;

    private TabItem moneyOwedTabItem;
    private ListView moneyOwedListView;
    private TextView moneyOwedAmtTextView;
    private TextView moneyOwedPersonTextView;
    private ImageButton moneyOwedSettleUpButton;
    private SimpleAdapter simpleAdapter;
    private HashMap<String,Double> owedTransactions;
    private HashMap<String,String> owedTransactionsNames;
    private ArrayList<HashMap<String,String>>data;

    public MoneyOwedFragment() {
        // Required empty public constructor
    }

    public static MoneyOwedFragment newInstance(String param1, String param2) {
        MoneyOwedFragment fragment = new MoneyOwedFragment();
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
        owedTransactions = new HashMap<>();
        owedTransactionsNames = new HashMap<>();
        data = new ArrayList<>();
        Log.d(TAG, "onCreate: ");
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getOwedTransactions(userMobile);
        View view =  inflater.inflate(R.layout.fragment_money_owed, container, false);
        moneyOwedTabItem = (TabItem) view.findViewById(R.id.owed_tab_item);
        moneyOwedListView = (ListView) view.findViewById(R.id.money_owed_listview);
        moneyOwedPersonTextView = (TextView) view.findViewById(R.id.moneyOwedPerson);
        moneyOwedPersonTextView = (TextView) view.findViewById(R.id.moneyOwedAmt);
        moneyOwedSettleUpButton = (ImageButton) view.findViewById(R.id.moneyOwedSettleUp);
        moneyOwedListView.setOnItemClickListener(this);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getOwedTransactions(String userMobile){
        Log.d(TAG, "getOwedTransactions: "+userMobile);
        rf_t.whereEqualTo("fromID",userMobile)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        owedTransactions.clear();
                        owedTransactionsNames.clear();
                        data.clear();
                        Log.d(TAG, "onEvent: in snapShot getOwedTrans "+queryDocumentSnapshots.size());
                        for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                            double amount = C.round(doc.getDouble("amount"));
                            String toID = doc.getString("toID");
                            String to = doc.getString("to");
                            double prev_amount = owedTransactions.containsKey(toID) ? owedTransactions.get(toID) : 0;
                            owedTransactions.put(toID, C.round(prev_amount + amount));
                            owedTransactionsNames.put(toID,to);
                        }
                        updateList();
                    }
                });
    }

    private void updateList(){
        for(Map.Entry<String, Double> entry : owedTransactions.entrySet()){
            HashMap<String,String> map = new HashMap<>();
            map.put("to",owedTransactionsNames.get(entry.getKey()));
            map.put("toID",entry.getKey());
            map.put("amount",String.valueOf(entry.getValue()));
            data.add(map);
        }
        Log.d(TAG, "updateList: "+data.size());
        int resource = R.layout.money_owed_list;
        String[] from = {"to","toID", "amount"};
        int[] to = {R.id.moneyOwedPerson,R.id.moneyOwedPersonID, R.id.moneyOwedAmt};
        // create and set the adapter
        simpleAdapter=new SimpleAdapter(getActivity(),data,resource,from,to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ImageButton b = (ImageButton) v.findViewById(R.id.moneyOwedSettleUp);
                final String to = ((TextView) v.findViewById(R.id.moneyOwedPerson)).getText().toString();
                final String amt = ((TextView) v.findViewById(R.id.moneyOwedAmt)).getText().toString();
                final String toID = ((TextView) v.findViewById(R.id.moneyOwedPersonID)).getText().toString();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle(C.SETTLE_UP)
                                .setMessage("Are you sure you want to create a transaction to receive "+amt+" from "+to)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        createTransaction(to,toID,userName,userMobile,Double.valueOf(amt));
                                        activityDB.createActivity(userMobile,"You settled your debt with "+to+" by receiving -"+amt+"- AED",C.ACTIVITY_TYPE_SETTLE_UP);
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
        moneyOwedListView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    private void createTransaction(String from, String fromID, String to, String toID, double amount){
        final DocumentReference df = rf_t.document();
        Transactions transaction = new Transactions(from,fromID,to,toID,amount);
        df.set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                rf_u.document(userMobile).update(C.USERS_TRANSACTIONS, FieldValue.arrayUnion(df.getId()));
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SimpleAdapter adapter = (SimpleAdapter) parent.getAdapter();
        ListView currentLv = (ListView) view;
        Object item = adapter.getItem(position);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
