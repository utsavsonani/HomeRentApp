package com.example.homerentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homerentapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] arrUserType = {"User", "Broker"};
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        SharedPreferences.Editor sharedpre = getSharedPreferences(getString(R.string.pref_file_key),MODE_PRIVATE).edit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrUserType);
        binding.spUserType.setAdapter(adapter);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                if(inputValidation()){
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                sharedpre.putBoolean(getString(R.string.login_flag_key),true);
                                sharedpre.apply();
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "Authentication failed " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        binding.btnNewAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
            }
        });

    }

    boolean inputValidation(){
        boolean inputValidation = true;
        binding.email.setError(null);
        binding.password.setError(null);
        if (binding.email.getText().toString().isEmpty()) {
            binding.email.setError(getString(R.string.email_is_required));
            inputValidation = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            binding.email.setError(getString(R.string.enter_valid_email_address));
            inputValidation = false;
        }
        if (binding.password.getText().toString().isEmpty()) {
            binding.password.setError(getString(R.string.password_is_required));
            inputValidation = false;
        } else if (binding.password.getText().toString().length() < 6 || binding.password.getText().toString().length() > 10) {
            binding.password.setError(getString(R.string.password_length));
            inputValidation = false;
        }
        return inputValidation;
    }

}