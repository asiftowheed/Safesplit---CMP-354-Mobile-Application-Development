package com.example.b00063271.safesplit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.b00063271.safesplit.Database.C;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    private final String TAG = "DashboardFrag";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rf_u = db.collection(C.COLLECTION_USERS);

    private String userMobile;
    private String userName;

    private ArrayList<String> activities;
    private ArrayList<Integer> activityType;
    private ListView dashboardListView;
    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities = new ArrayList<>();
        activityType = new ArrayList<>();
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        getDashboardActivity(userMobile);
        dashboardListView = (ListView) v.findViewById(R.id.dashboardListView);
        return v;
    }

    private void getDashboardActivity(String userMobile){
        rf_u.document(userMobile).collection(C.COLLECTION_USERS_HISTORY)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                activities.clear();
                activityType.clear();
                for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    activities.add(doc.getString(C.USERS_HISTORY_ACTIVITY));
                    int type = (int)Math.round(doc.getDouble(C.USERS_HISTORY_TYPE));
                    switch(type){
                        case C.ACTIVITY_TYPE_SETTLE_UP:
                            activityType.add(R.drawable.baseline_accessibility_black_18dp);
                            break;
                            default:
                                Log.d(TAG, "onEvent: Type not found");
                    }
                }
                updateList();
            }
        });
    }

    private void updateList() {
        dashboardListView.setAdapter(new CustomAdapter(this, activities,activityType));
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
    public interface OnFragmentInteractionListener { void onFragmentInteraction(Uri uri); }
}
