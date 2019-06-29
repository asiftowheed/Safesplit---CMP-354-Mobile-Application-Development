package com.example.b00063271.safesplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00063271.safesplit.FriendsFragment.MainFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOweFragment;
import com.example.b00063271.safesplit.FriendsFragment.MoneyOwedFragment;
import com.example.b00063271.safesplit.FriendsFragment.TotalBalanceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.List;

import static com.example.b00063271.safesplit.AddBill.UpdateView;
import static com.example.b00063271.safesplit.AddBill.amount;
import static com.example.b00063271.safesplit.AddBill.payers;
import static com.example.b00063271.safesplit.AddBill.users;

public class SplitActivity extends AppCompatActivity implements splitpercent.OnFragmentInteractionListener,splitexactamounts.OnFragmentInteractionListener,
            splitequally.OnFragmentInteractionListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String userID="xJNsNNf39VJ62aiETsiO";
    private String username = "hussu";
    private RadioGroup rg;
    private RadioButton rbequal, rbexact, rbpercent;
    private TextView fragment_title;
    private ListView participants;
    static int split_type = 0;


    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        rg = (RadioGroup) findViewById(R.id.rg_split);
        rbequal = (RadioButton) findViewById(R.id.radio0);
        rbexact = (RadioButton) findViewById(R.id.radio1);
        rbpercent = (RadioButton) findViewById(R.id.radio2);
        fragment_title = (TextView) findViewById(R.id.textView3);
        participants = (ListView) findViewById(R.id.equalpayerslist);

        fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.splitOptionsViewPager);
        setupViewPager();

/*
        fragment_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, true);
                rbpercent.setChecked(true);
            }
        });
*/


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio0:{
                        viewPager.setCurrentItem(0, true);
                        break;
                    }
                    case R.id.radio1:{
                        viewPager.setCurrentItem(1, true);
                        break;
                    }
                    case R.id.radio2:{
                        viewPager.setCurrentItem(2, true);
                        break;
                    }
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:{
                        rbequal.setChecked(true);
                        break;
                    }
                    case 1:{
                        rbexact.setChecked(true);
                        break;
                    }
                    case 2:{
                        rbpercent.setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(){
        MainFragment.Adapter adapter = new MainFragment.Adapter(fragmentManager);
        adapter.addFragment(splitequally.newInstance("",""), "Equal");
        adapter.addFragment(splitexactamounts.newInstance("",""), "Exact");
        adapter.addFragment(splitpercent.newInstance("",""), "Percent");
        viewPager.setAdapter(adapter);
    }
    private boolean openFragment(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("abcd", "onFragmentInteraction: "+uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.custompayment, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.done:{
                Float sum = 0f;
                payers.clear();
                for (int i = 0; i < users.size(); i++){
                    View wantedView = participants.getChildAt(i);
                    EditText individual_ED = (EditText) wantedView.findViewById(R.id.payed_amount);
                    Float individual_amount;
                    if (individual_ED.getText() != null && !individual_ED.getText().toString().equals("") && Float.parseFloat(individual_ED.getText().toString()) != 0f){
                        individual_amount = Float.parseFloat(individual_ED.getText().toString());
                        sum += individual_amount;
                        payers.put(users.get(i), individual_amount);
                        System.out.println(users.get(i) + " added to the list --------------------");
                    }
                }
                if (sum < Float.parseFloat(amount.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Insufficient amount entered!", Toast.LENGTH_SHORT).show();
                }
                else if (sum > Float.parseFloat(amount.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Excess amount entered!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Iterator itt = payers.keySet().iterator();
                    System.out.println(payers.size() + "===========================================");
                    for (int i = 0; i < payers.size(); i++){
                        String key = (String)itt.next();
                        System.out.print(key);
                        System.out.print(" ==> ");
                        System.out.println(payers.get(key));
                    }
                    System.out.println("===========================================");
                    UpdateView();
                    finish();
                }
            }
        }
        return true;
    }



}
