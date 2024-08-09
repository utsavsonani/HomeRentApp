package com.example.homerentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.homerentapp.databinding.ActivityHomeBinding;
import com.example.homerentapp.databinding.ActivityPostHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostHomeActivity extends AppCompatActivity {

    ActivityPostHomeBinding binding;
//    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPostHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new HomeFragment());
        binding.bottomnavigationView.setBackground(null);
//        floatingActionButton = findViewById(R.id.Add_Homed);



        binding.bottomnavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.Home) {
                replaceFragment(new HomeFragment());
                return true;
            }

            if(item.getItemId() == R.id.Profile) {
                replaceFragment(new profile_broker_ragment());
                return true;
            }

            return true;
        });

        binding.AddHomed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostHomeActivity.this, PostPropertyActivity.class));
            }
        });

    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}