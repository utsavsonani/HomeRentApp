package com.example.homerentapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.homerentapp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;



public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;
//    DrawerLayout drawerLayout;
    ImageButton imageButtonToggle;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getMenuInflater().inflate(R.menu.drawer_items, binding.toolbar.getMenu());



//        toolbar  = findViewById(R.id.toolbar);
//        drawerLayout   = findViewById(R.id.drawerLayout);

//        imageButtonToggle = findViewById(R.id.buttonDrawerToggle);

//        imageButtonToggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.open();
//            }
//        });

        setSupportActionBar(binding.toolbar);

//        binding.navigationview.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.open_navigation_drawer,R.string.colse_navigation_drawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationview.setNavigationItemSelectedListener(this);


    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }



}