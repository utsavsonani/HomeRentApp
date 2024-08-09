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
import com.example.homerentapp.util.HomeRentApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userType = "";
    private DatabaseReference database;
    private FirebaseUser currentUser;

//    conneation to firestore
    private final FirebaseFirestore firebaseFirestore  = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReferenceForuser = firebaseFirestore.collection("Users");
    private final CollectionReference collectionReferenceForbroker = firebaseFirestore.collection("Broker");
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
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        SharedPreferences.Editor sharedpre = getSharedPreferences(getString(R.string.pref_file_key),MODE_PRIVATE).edit();

        Log.d("akak", "" + firebaseAuth.getCurrentUser());

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




//        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = binding.name.getText().toString();
//                String email = binding.email.getText().toString();
//                String mNumber = binding.mobileNumber.getText().toString();
//                String password = binding.password.getText().toString();
//
//                if (inputValidation()) {
//                       auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()){
//                                String userId = auth.getUid();
//                                User userData = new User(name, email, mNumber, userType, password);
//
//                                database.child(userData.getUserType()).child(userId).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                                        sharedpre.putBoolean(getString(R.string.login_flag_key),true);
//                                        sharedpre.apply();
//                                        Toast.makeText(RegistrationActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(RegistrationActivity.this, " " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(RegistrationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Enter Valid Information", Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputValidation()) {
                    String userName = binding.name.getText().toString();
                    String email = binding.email.getText().toString();
                    String PhoneNumber = binding.mobileNumber.getText().toString();
                    String password = binding.password.getText().toString();
                    creatUserWithEmailandPassword(userName,email,PhoneNumber,userType,password);
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

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null) {
//                    user is exist
                } else {
//                 user does not exist
                }
            }
        };

    }

    private void creatUserWithEmailandPassword(String userName, String email, String phoneNumber, String userType, String password) {

        if(Objects.equals(userType, "User")) {
//            creat a user
            binding.progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserid = currentUser.getUid();

                                Map<String,String> userobj =  new HashMap<>();
                                userobj.put("userId",currentUserid);
                                userobj.put("userName",userName);
                                userobj.put("phoneNumber",phoneNumber);
                                userobj.put("userType",userType);

//                                store data in firestore

                                collectionReferenceForuser.add(userobj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
//                                                 useing documnetReference to get data for Homerent  and documentReference it refer our userobj colleation
//                                                 so using it to make new colleation and it get the current user information
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                it refer to your user and in data of documentReference so it using task to get username
                                                                if(task.getResult().exists()) {
                                                                    String name = task.getResult().getString("userName");
                                                                    String userId = task.getResult().getString("userId");
                                                                    String phone = task.getResult().getString("phoneNumber");
                                                                    String usertype = task.getResult().getString("userType");
                                                                    HomeRentApi homeRentApi = HomeRentApi.getInstance();
                                                                    homeRentApi.setUserName(name);
                                                                    homeRentApi.setPhoneNumber(phone);
                                                                    homeRentApi.setUserId(userId);
                                                                    homeRentApi.setUserType(usertype);

                                                                    binding.progressBar.setVisibility(View.INVISIBLE);

                                                                    startActivity(new Intent(RegistrationActivity.this, UserHomeActivity.class));
                                                                    finish();
                                                                }

                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                binding.progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(),"onFailure : "+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    });
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String currentUserid = currentUser.getUid();

                                Map<String,String> userobj =  new HashMap<>();
                                userobj.put("brokerId",currentUserid);
                                userobj.put("brokerName",userName);
                                userobj.put("phoneNumber",phoneNumber);
                                userobj.put("userType",userType);

//                                store data in firestore

                                collectionReferenceForbroker.add(userobj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
//                                                 useing documnetReference to get data for Homerent  and documentReference it refer our userobj colleation
//                                                 so using it to make new colleation and it get the current user information
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                it refer to your user and in data of documentReference so it using task to get username
                                                                if(task.getResult().exists()) {
                                                                    String name = task.getResult().getString("userName");
                                                                    String userId = task.getResult().getString("userId");
                                                                    String phone = task.getResult().getString("phoneNumber");
                                                                    String usertype = task.getResult().getString("userType");
                                                                    HomeRentApi homeRentApi = HomeRentApi.getInstance();
                                                                    homeRentApi.setUserName(name);
                                                                    homeRentApi.setPhoneNumber(phone);
                                                                    homeRentApi.setUserId(userId);
                                                                    homeRentApi.setUserType(usertype);

                                                                    binding.progressBar.setVisibility(View.INVISIBLE);

                                                                    startActivity(new Intent(RegistrationActivity.this, UserHomeActivity.class));
                                                                    finish();
                                                                }

                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                binding.progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(),"onFailure : "+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
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
        } else if (binding.password.getText().toString().length() < 6 || binding.password.getText().toString().length() > 16) {
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