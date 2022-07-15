package com.example.bloodred;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class EmailActivity extends AppCompatActivity {

    TextInputEditText To,Subject,Message;
    Button EmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);


        To=findViewById(R.id.To);
        Subject=findViewById(R.id.Subject);
        Message=findViewById(R.id.Message);
        EmailButton=findViewById(R.id.EmailButton);


        EmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to=To.getText().toString().trim();
                String sub=Subject.getText().toString().trim();
                String message=Message.getText().toString().trim();

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, new String[]{sub});
                intent.putExtra(Intent.EXTRA_TEXT, new String[]{message});
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Choose an Email Client"));
            }
        });
    }
}