package com.example.homerentapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.homerentapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class profile_broker_ragment extends Fragment {

    FragmentProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_profile_broker_ragment, container, false);



        return view;


    }
}