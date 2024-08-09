package com.example.homerentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homerentapp.databinding.ActivityLoginBinding;
import com.example.homerentapp.util.HomeRentApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
//    private FirebaseAuth auth;
//    private DatabaseReference database;

    private  FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

//    firestore collation

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReferenceForUser = firebaseFirestore.collection("Users");
    private final CollectionReference collectionReferenceForBroker = firebaseFirestore.collection("Broker");

    String userType = "";

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
        firebaseAuth = FirebaseAuth.getInstance();
//        Fire
//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance().getReference();
        SharedPreferences.Editor sharedpre = getSharedPreferences(getString(R.string.pref_file_key),MODE_PRIVATE).edit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrUserType);
        binding.spUserType.setAdapter(adapter);

        binding.spUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = arrUserType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(LoginActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

//        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = binding.email.getText().toString();
//                String password = binding.password.getText().toString();
//                if(inputValidation()){
//                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
//                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                                sharedpre.putBoolean(getString(R.string.login_flag_key),true);
//                                sharedpre.apply();
//                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }else {
//                                Toast.makeText(LoginActivity.this, "Authentication failed " + task.getException(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//
//            }
//        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                loginUisngEmailAndPassword(email,password);
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

    private void loginUisngEmailAndPassword(String email, String password) {
        if(inputValidation()) {
            if(Objects.equals(userType, "User")) {
                binding.progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String userId = firebaseUser.getUid();

                                collectionReferenceForUser.whereEqualTo("userId",userId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if(error != null) {
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),"something worng " +error.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                                if (value != null && !value.isEmpty()) {
                                                    for (QueryDocumentSnapshot snapshot : value) {
                                                        HomeRentApi homeRentApi = HomeRentApi.getInstance();
                                                        homeRentApi.setUserId(userId);
                                                        homeRentApi.setUserName(snapshot.getString("userName"));
                                                        homeRentApi.setPhoneNumber(snapshot.getString("phoneNumber"));
                                                        homeRentApi.setUserType(snapshot.getString("userType"));

                                                        binding.progressBar.setVisibility(View.INVISIBLE);

                                                        startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                                                        finish();
                                                    }
                                                } else {
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),"something worng " ,Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"something worng " +e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                String BrokerId = firebaseUser.getUid();

                                collectionReferenceForBroker.whereEqualTo("brokerId",BrokerId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if(error != null) {
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),"something worng " +error.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                                if (value != null && !value.isEmpty()) {
                                                    for (QueryDocumentSnapshot snapshot : value) {
                                                        HomeRentApi homeRentApi = HomeRentApi.getInstance();
                                                        homeRentApi.setUserId(BrokerId);
                                                        homeRentApi.setUserName(snapshot.getString("brokerName"));
                                                        homeRentApi.setPhoneNumber(snapshot.getString("phoneNumber"));
                                                        homeRentApi.setUserType(snapshot.getString("userType"));

                                                        binding.progressBar.setVisibility(View.INVISIBLE);

                                                        startActivity(new Intent(LoginActivity.this, PostHomeActivity.class));
                                                        finish();
                                                    }
                                                } else {
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(),"something worng " ,Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"something worng " +e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
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
        } else if (binding.password.getText().toString().length() < 6 || binding.password.getText().toString().length() > 16) {
            binding.password.setError(getString(R.string.password_length));
            inputValidation = false;
        }
        return inputValidation;
    }

}