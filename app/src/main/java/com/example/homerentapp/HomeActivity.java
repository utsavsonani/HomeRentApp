package com.example.homerentapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.homerentapp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity{

    private ActivityHomeBinding binding;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        SharedPreferences.Editor sharedPre = getSharedPreferences(getString(R.string.pref_file_key),MODE_PRIVATE).edit();
        auth = FirebaseAuth.getInstance();

        toolbar  = findViewById(R.id.toolbar);
        drawerLayout   = findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.colse_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("akak", "" + item);
                Log.d("akak", "" + item.getItemId());
                if(item.getItemId() == R.id.Home){
                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    return true;
                } 
                else if(item.getItemId() == R.id.Profile){
                    Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Nearby){
                    Toast.makeText(HomeActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Saved){
                    Toast.makeText(HomeActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Notification){
                    Toast.makeText(HomeActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Message){
                    Toast.makeText(HomeActivity.this, "Message", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Setting){
                    Toast.makeText(HomeActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Help){
                    Toast.makeText(HomeActivity.this, "Help", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(item.getItemId() == R.id.Logout){
                    auth.signOut();
                    sharedPre.putBoolean(getString(R.string.login_flag_key),false);
                    sharedPre.apply();
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


}