package com.example.b00063271.safesplit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.b00063271.safesplit.AddBill.UpdateView;
import static com.example.b00063271.safesplit.AddBill.amount;
import static com.example.b00063271.safesplit.AddBill.payers;

public class paidByActivity extends Activity {

    ListView participants;
    ArrayList<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_by);

        participants = (ListView) findViewById(R.id.participants);

        users = getIntent().getStringArrayListExtra("users");

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, users);

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (String user:users){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", user);
            data.add(map);
        }


        int resource = R.layout.list_item;
        String[] from = {"name"};
        int[] to = {R.id.titleTextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        participants.setAdapter(adapter);

        participants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    finish();
                    Intent customintent = new Intent(getApplicationContext(), CustomPayment.class);
                    customintent.putExtra("users", users);
                    startActivity(customintent);
                }
                else if (position == 1){
                    //paidby.setText("You");
                    payers.clear();
                    payers.put("You", Float.parseFloat(amount.getText().toString()));
                    UpdateView();
                    finish();
                }
                else{
                    //paidby.setText(users.get(position));
                    payers.clear();
                    payers.put(users.get(position),Float.parseFloat(amount.getText().toString()));
                    UpdateView();
                    finish();
                }
            }
        });
    }
}
