package com.example.homerentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homerentapp.util.HomeRentApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore firebaseFirestore  = FirebaseFirestore.getInstance();

    private CollectionReference collectionReferenceForUser = firebaseFirestore.collection("Users");
    private CollectionReference collectionReferenceForBroker = firebaseFirestore.collection("Broker");

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

//        SharedPreferences sharedPre = getSharedPreferences(getString(R.string.pref_file_key),MODE_PRIVATE);
//        Boolean check = sharedPre.getBoolean(getString(R.string.login_flag_key),false);



        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null) {

                     String userId = currentUser.getUid();

                    collectionReferenceForUser.whereEqualTo("userId",userId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if(error != null) {
                                        Toast.makeText(getApplicationContext(),"something worng",Toast.LENGTH_LONG).show();
                                    }

                                    if(value != null && !value.isEmpty()) {
                                        for(QueryDocumentSnapshot snapshot : value) {
                                            HomeRentApi homeRentApi = HomeRentApi.getInstance();
                                            homeRentApi.setUserId(snapshot.getString("userId"));
                                            homeRentApi.setUserName(snapshot.getString("userName"));
                                            homeRentApi.setPhoneNumber(snapshot.getString("phoneNumber"));
                                            homeRentApi.setUserType(snapshot.getString("userType"));

                                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                                            finish();
                                        }

                                    }else{
//                                        check in broker collection
                                        checkBrokerCollection(userId);
                                    }
                                }
                            });

                } else {
                    startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
                    finish();
//                    Toast.makeText(getApplicationContext(),"something worng",Toast.LENGTH_LONG).show();
                }
            }
        };


    }

    private void checkBrokerCollection(String userId) {
//        String brokerId = currentUser.getUid();

        collectionReferenceForBroker.whereEqualTo("brokerId",userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Toast.makeText(getApplicationContext(),"something worng " +error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        if(value != null && !value.isEmpty()) {
                            for(QueryDocumentSnapshot snapshot : value) {
                                HomeRentApi homeRentApi = HomeRentApi.getInstance();
                                homeRentApi.setBrokerId(userId);
                                homeRentApi.setBrokerName(snapshot.getString("brokerName"));
                                homeRentApi.setPhoneNumber(snapshot.getString("phoneNumber"));
                                homeRentApi.setUserType(snapshot.getString("userType"));

                                startActivity(new Intent(SplashScreenActivity.this, PostHomeActivity.class));
                                finish();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(),"something worng",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
                            finish();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            currentUser = firebaseAuth.getCurrentUser();
            firebaseAuth.addAuthStateListener(authStateListener);
        },3500);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }

    }


}