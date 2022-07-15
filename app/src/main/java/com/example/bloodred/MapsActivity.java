package com.example.bloodred;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapsActivity extends AppCompatActivity {

    Button MapsButton;
    ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        container=findViewById(R.id.container);

        MapsButton=findViewById(R.id.MapsButton);
        MapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsButton.setVisibility(View.GONE);
                Fragment fragment=new MapsFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment).commit();
            }
        });




    }
}