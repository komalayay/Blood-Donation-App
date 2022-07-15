package com.example.bloodred;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogOutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView registerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        registerEmail=findViewById(R.id.registerEmail);


        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser= mAuth.getCurrentUser();
        if(mFirebaseUser!=null)
        {
            //there is some user logged in
            registerEmail.setText(mFirebaseUser.getEmail());
        }
        else{
            //no one logged in
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent= new Intent(LogOutActivity.this,ChooseActivity.class);
        startActivity(intent);
    }
}