package com.example.bloodred;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChooseActivity extends AppCompatActivity {

    private TextView donor;
    private TextView customer;

    private TextView AlreadyHaveAnAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);




        donor=findViewById(R.id.donor);
        customer=findViewById(R.id.customer);
        AlreadyHaveAnAccount=findViewById(R.id.AlreadyHaveAnAccount);


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChooseActivity.this,CustomerRegActivity.class);
                startActivity(intent);
            }
        });


        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChooseActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChooseActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}