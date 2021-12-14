package com.example.wineapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;

import java.util.LinkedList;
import java.util.List;


public class SignInFragment extends Fragment {
    User user;
    List<User> users;
    EditText name, password;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button enterBtn = view.findViewById(R.id.register_sign_up_btn);
        name=view.findViewById(R.id.sign_in_username_et);
        password=view.findViewById(R.id.sign_in_password_et);
        progressBar= view.findViewById(R.id.sign_in_progressbar);
        progressBar.setVisibility(View.GONE);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                users=Model.instance.getUsersList();
                int positionUser=Model.instance.getUserPosition(name.getText().toString());
                user=users.get(positionUser);
                if(ChekPassword(password.getText().toString())&&ChekUserName(name.getText().toString())){
                    SignInFragmentDirections.ActionSignInFragmentToListPostFragment action=SignInFragmentDirections.actionSignInFragmentToListPostFragment(user);
                    Navigation.findNavController(v).navigate(action);
                }
                else if(ChekPassword(password.getText().toString())){
                    name.setBackgroundColor(Color.RED);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    password.setBackgroundColor(Color.RED);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        setHasOptionsMenu(true);
        return view;
    }


    private boolean ChekPassword(String password){
        return password.equals(user.getPassword());
    }
    private boolean ChekUserName(String name){return name.equals(user.getName());}
}