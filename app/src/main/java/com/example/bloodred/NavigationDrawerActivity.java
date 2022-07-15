package com.example.bloodred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout DrawerLayout;
    private Toolbar toolbar;
    private NavigationView NavigationView;

    private CircleImageView NavigationProfile;
    private TextView NavUserName,NavUserEmail,NavUserType,NavUserBloodGroup;
    private DatabaseReference DatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Red Blood");



        DrawerLayout = findViewById(R.id.DrawerLayout);
        NavigationView = findViewById(R.id.NavigationView);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(NavigationDrawerActivity.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_Close);
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView.setNavigationItemSelectedListener(this);


        NavigationProfile=NavigationView.getHeaderView(0).findViewById(R.id.NavigationProfile);
        NavUserBloodGroup=NavigationView.getHeaderView(0).findViewById(R.id.NavUserBloodgroup);
        NavUserName=NavigationView.getHeaderView(0).findViewById(R.id.NavUserName);
        NavUserEmail=NavigationView.getHeaderView(0).findViewById(R.id.NavUserEmail);
        NavUserType=NavigationView.getHeaderView(0).findViewById(R.id.NavUsertype);

        DatabaseRef= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        DatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    NavUserName.setText(name);

                    String bloodgroup =snapshot.child("bloodgroup").getValue().toString();
                    NavUserBloodGroup.setText(bloodgroup);

                    String type =snapshot.child("type").getValue().toString();
                    NavUserType.setText(type);

                    String email =snapshot.child("email").getValue().toString();
                    NavUserEmail.setText(email);

                   String profilepicture =snapshot.child("profilepicture").getValue().toString();
                        Glide.with(getApplicationContext()).load(profilepicture).into(NavigationProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.EmailMenu:
                Intent intent=new Intent(NavigationDrawerActivity.this,EmailActivity.class);
                startActivity(intent);
                break;

            case R.id.ProfileMenu:
                Intent intent2=new Intent(NavigationDrawerActivity.this,ProfileActivity.class);
                startActivity(intent2);
                break;

            case R.id.SearchMenu:
                Intent intent3=new Intent(NavigationDrawerActivity.this,SearchActivity.class);
                startActivity(intent3);
                break;

            case R.id.AboutMenu:
                Intent intent4=new Intent(NavigationDrawerActivity.this,AboutActivity.class);
                startActivity(intent4);
                break;

            case R.id.LogOutMenu:
                Intent intent5=new Intent(NavigationDrawerActivity.this,LogOutActivity.class);
                startActivity(intent5);
                break;

            case R.id.GoogleMapsMenu:
                Intent intent6=new Intent(NavigationDrawerActivity.this,MapsActivity.class);
                startActivity(intent6);
                break;

            case R.id.EligibilityMenu:
                Intent intent7=new Intent(NavigationDrawerActivity.this,EligibilityActivity.class);
                startActivity(intent7);
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
