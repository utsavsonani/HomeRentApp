package com.example.homerentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.homerentapp.databinding.ActivityRegistrationBinding;
import com.example.homerentapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;


public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private FirebaseAuth auth;
    private String userType = "";
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
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

        Log.d("akak", "" + auth.getCurrentUser());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrUserType);
        binding.spUserType.setAdapter(adapter);

        binding.spUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = arrUserType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegistrationActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String mNumber = binding.mobileNumber.getText().toString();
                String password = binding.password.getText().toString();

                if (inputValidation()) {
                       auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String userId = auth.getUid();
                                User userData = new User(name, email, mNumber, userType, password);

                                database.child(userData.getUserType()).child(userId).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                        sharedpre.putBoolean(getString(R.string.login_flag_key),true);
                                        sharedpre.apply();
                                        Toast.makeText(RegistrationActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegistrationActivity.this, " " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Information", Toast.LENGTH_LONG).show();
                }


            }
        });

        binding.btnAlreadyAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

    boolean inputValidation(){
        boolean inputValidation = true;
        binding.name.setError(null);
        binding.email.setError(null);
        binding.mobileNumber.setError(null);
        binding.password.setError(null);
        binding.confirmPwd.setError(null);
        if (binding.name.getText().toString().isEmpty()) {
            binding.name.setError(getString(R.string.name_is_required));
            inputValidation = false;
        }
        if (binding.email.getText().toString().isEmpty()) {
            binding.email.setError(getString(R.string.email_is_required));
            inputValidation = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            binding.email.setError(getString(R.string.enter_valid_email_address));
            inputValidation = false;
        }
        if (binding.mobileNumber.getText().toString().isEmpty()) {
            binding.mobileNumber.setError(getString(R.string.mNumber_is_required));
            inputValidation = false;
        } else if (binding.mobileNumber.getText().toString().length() < 10) {
            binding.mobileNumber.setError(getString(R.string.enter_valid_mobile_number));
            inputValidation = false;
        }
        if (binding.password.getText().toString().isEmpty()) {
            binding.password.setError(getString(R.string.password_is_required));
            inputValidation = false;
        } else if (binding.password.getText().toString().length() < 6 || binding.password.getText().toString().length() > 10) {
            binding.password.setError(getString(R.string.password_length));
            inputValidation = false;
        }
        if (binding.confirmPwd.getText().toString().isEmpty()) {
            binding.confirmPwd.setError(getString(R.string.confirmPwd_is_required));
            inputValidation = false;
        }

        if (!binding.password.getText().toString().equals(binding.confirmPwd.getText().toString())) {
            binding.password.setError(getString(R.string.both_password_must_be_match));
            binding.confirmPwd.setError(getString(R.string.both_password_must_be_match));
            inputValidation = false;
        }

        return inputValidation;
    }
}