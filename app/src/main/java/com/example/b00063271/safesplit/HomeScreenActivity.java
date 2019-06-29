package com.example.b00063271.safesplit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.b00063271.safesplit.FriendsFragment.MainFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOweFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOwedFragment;
import com.example.b00063271.safesplit.FriendsFragment.TotalBalanceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeScreenActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,DashboardFragment.OnFragmentInteractionListener
                ,ProfileFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,MoneyOweFragment.OnFragmentInteractionListener, MoneyOwedFragment.OnFragmentInteractionListener,
        TotalBalanceFragment.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListView listView;
    private final String TAG="HSActivity";
    private String userMobile="xJNsNNf39VJ62aiETsiO";
    private String userName ="Hussu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Intent intent = getIntent();
        userMobile = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        if(userMobile==null){
            userMobile="12345";
            userName="Hussu";
        }
        fragmentManager = getSupportFragmentManager();
        listView = (ListView)findViewById(R.id.money_owed_listview);
        openFragment(MainFragment.newInstance(userMobile,userName));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.naviagation_friends:
                    Log.d(TAG, "onNavigationItemSelected: "+userMobile+" "+userName);
                    return openFragment(MainFragment.newInstance(userMobile,userName));
                case R.id.navigation_dashboard:
                    return openFragment(DashboardFragment.newInstance(userMobile,userName));
                case R.id.naviagtion_me:
                    return openFragment(new ProfileFragment());
                case R.id.navigation_groups:
                    return openFragment(new GroupsFragment());
                case R.id.navigation_new_bill:
                    startActivity(new Intent(getApplicationContext(),AddUsers.class));
                    overridePendingTransition(R.anim.push_bottom_up,R.anim.remain_same_position);
                    return true;
            }
            return false;
        }
    };
    private boolean openFragment(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction: "+uri);
    }
}
