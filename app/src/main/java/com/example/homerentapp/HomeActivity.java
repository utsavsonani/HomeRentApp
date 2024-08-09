package com.example.homerentapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.homerentapp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity{


    private ActivityHomeBinding binding;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();

        toolbar  = findViewById(R.id.toolbar);
        drawerLayout   = findViewById(R.id.drawerLayout);
        profile_image = findViewById(R.id.Profile_image);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.colse_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        binding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("akak", "" + item);
                Log.d("akak", "" + item.getItemId());
                if(item.getItemId() == R.id.Home){
                    return true;
                } 
                else if(item.getItemId() == R.id.Profile){
                    replaceFragment(new ProfileFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Nearby){
                    replaceFragment(new NearbyFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Saved){
                    replaceFragment(new SavedFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Notification){
                    replaceFragment(new NotificationFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Message){
                    replaceFragment(new ProfileFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Setting){
                    replaceFragment(new SettingFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Help){
                    replaceFragment(new ProfileFragment());
                    drawerLayout.close();
                    return true;
                }
                else if(item.getItemId() == R.id.Logout){

                    firebaseAuth.signOut();
                    Toast.makeText(HomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    finish();
                    return true;
                } 
                else {
                    return false;
                }
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

}