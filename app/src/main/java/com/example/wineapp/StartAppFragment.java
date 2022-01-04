package com.example.wineapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartAppFragment extends Fragment {
    Button registerBtn,signInBtn;
    View view;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_start_app, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Model.instance.getUserByEmail(user.getEmail(), new Model.GetUserByEmailListener() {
                @Override
                public void onComplete(User u) {
                    StartAppFragmentDirections.ActionStartAppFragmentToListPostFragment action=StartAppFragmentDirections.actionStartAppFragmentToListPostFragment(u);
                    Navigation.findNavController(view).navigate(action);
                }
            });

        }
        registerBtn= view.findViewById(R.id.start_app_register_btn);
        signInBtn = view.findViewById(R.id.start_app_signin_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_startAppFragment_to_registerFragment);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_startAppFragment_to_signInFragment);
            }
        });

        setHasOptionsMenu(true);
        return view;
    }
}