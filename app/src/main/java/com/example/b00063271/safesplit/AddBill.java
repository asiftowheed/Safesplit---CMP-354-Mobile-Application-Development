package com.example.b00063271.safesplit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class AddBill extends AppCompatActivity {

    // PRIVATES
    private Spinner splitSpinner;
    private AlertDialog.Builder b;

    // STATICS
    static Button paidby;
    static Button split;
    static EditText amount;
    private Boolean First = true;



    static ArrayList<String> users;
    static HashMap<String, Float> payers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        paidby = (Button) findViewById(R.id.paidbybutton);
        split = (Button) findViewById(R.id.splitbutton);
        amount = (EditText) findViewById(R.id.amount);

        // Get Users
        users = new ArrayList<String>();

        // Get Payers
        payers = new HashMap<>();

        if(First){
            payers.put("You", 0f);
            First = false;
            paidby.setText("You");

            users.add("Custom");
            users.add("You");
        }
        else{

        }

        ArrayList<String> fromprev = getIntent().getStringArrayListExtra("users");
        for(int i = 0; i < fromprev.size(); i++)
            users.add(fromprev.get(i));

        paidby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // b.show();
                    Intent dialogintent = new Intent(getApplicationContext(), paidByActivity.class);
                    dialogintent.putExtra("users", users);
                    startActivity(dialogintent);
                }
            }
        });

        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), SplitActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    static void UpdateView(){
        if (payers.size() == 1){
            paidby.setText(payers.keySet().iterator().next());
        }
        else{
            paidby.setText("Custom");

        }
    }

}
