package com.example.homerentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPre = getSharedPreferences(getString(R.string.pref_file_key),MODE_PRIVATE);
        Boolean check = sharedPre.getBoolean(getString(R.string.login_flag_key),false);

        new Handler().postDelayed(() -> {
            if (check) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }else {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
            }
        },3500);

    }
}