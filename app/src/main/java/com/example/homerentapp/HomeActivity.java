package com.example.homerentapp;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.homerentapp.R;

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

        final int profile = R.id.Profile;
        final int nearby = R.id.Nearby;
        final int saved = R.id.Saved;
        final int notification = R.id.Notification;
        final int message = R.id.Message;
        final int setting = R.id.Setting;
        final int help = R.id.Help;
        final int logout = R.id.Logout;



//        int a = R.id.Home;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int home  = R.id.Home;
        String hometext = String.valueOf(R.string.home)      ;
        Toast.makeText(this, hometext, Toast.LENGTH_SHORT).show();


        switch (item.getItemId()) {

//            case home:
//                // Handle the Home menu item click
//                Toast.makeText(this, "Home clicked!", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.Profile:
//                // Handle the Profile menu item click
//                Toast.makeText(this, "Profile clicked!", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.Nearby:
//                // Handle the Nearby menu item click
//                Toast.makeText(this, "Nearby clicked!", Toast.LENGTH_SHORT).show();
//                break;
//            // Add other cases for remaining menu items
        }
        return true;
    }




}