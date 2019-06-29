package com.example.b00063271.safesplit.FriendsFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b00063271.safesplit.Database.C;
import com.example.b00063271.safesplit.Database.TransactionDB;
import com.example.b00063271.safesplit.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "FriendsMainFrag";
    private final int MONEY_OWED_TAB =0;
    private final int MONEY_OWE_TAB =1;
    private final int TOTAL_BALANCE_TAB =2;

    private String userMobile;
    private String userName;

    private ArrayList<Double> moneyOwedTransactions;
    private ArrayList<Double> moneyOweTransactions;
    private ArrayList<Double> totalBalanceTransactions;
    private double moneyOwe=0;
    private double moneyOwed=0;
    private double totalBalance=0;

    private OnFragmentInteractionListener mListener;
    private TransactionDB transactionDB;
    private TransactionDB.OnDatabaseInteractionListener mDBListener= new TransactionDB.OnDatabaseInteractionListener() {
        @Override
        public void onDatabaseInteration(int requestCode, ArrayList<Double> a, ArrayList<Double> b, ArrayList<Double> c) {
            if(requestCode== C.CALLBACK_GET_TRANSACTIONS){
                moneyOwedTransactions = a;
                moneyOweTransactions = b;
                totalBalanceTransactions = c;
                updateLists();
            }
        }
    };
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String userID,String userName) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userID);
        args.putString(ARG_PARAM2, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        moneyOwedTransactions=new ArrayList<>();
        moneyOweTransactions=new ArrayList<>();
        totalBalanceTransactions=new ArrayList<>();
        transactionDB = new TransactionDB(mDBListener);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userMobile = getArguments().getString(ARG_PARAM1);
            userName = getArguments().getString(ARG_PARAM2);
        } else Log.d(TAG, "onCreate: Error, invalid Credentials");
        setRetainInstance(true);
    }
    private TabItem tabItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        transactionDB.getTransactions(userMobile);
        View view = inflater.inflate(R.layout.fragment_friends_main,container, false);
        viewPager = (ViewPager) view.findViewById(R.id.friends_view_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.friends_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void updateLists(){
        totalBalanceTransactions.clear();
        totalBalance=0;
        moneyOwed = 0;
        moneyOwe = 0;
        for(double amt:moneyOweTransactions){
            moneyOwe +=amt;
            amt=-1*amt;
            totalBalanceTransactions.add(amt);
            totalBalance+=amt;
        }
        for(double amt:moneyOwedTransactions){
            moneyOwed+=amt;
            totalBalanceTransactions.add(amt);
            totalBalance+=amt;
        }
        tabLayout.getTabAt(MONEY_OWED_TAB).setText("Money Owed "+C.round(moneyOwed));
        tabLayout.getTabAt(MONEY_OWE_TAB).setText("Money Owe "+C.round(moneyOwe));
        tabLayout.getTabAt(TOTAL_BALANCE_TAB).setText("Total balance "+C.round(totalBalance));
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(MoneyOwedFragment.newInstance(userMobile,userName), "Money Owed");
        adapter.addFragment(MoneyOweFragment.newInstance(userMobile,userName), "Money Owe");
        adapter.addFragment(TotalBalanceFragment.newInstance(userMobile,userName), "Total balance");
        viewPager.setAdapter(adapter);
    }

    public static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public Adapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
