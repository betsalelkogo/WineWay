package com.example.wineapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.User;


public class RegisterFragment extends Fragment {

    EditText name, password, confirmPassword,email;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button signUpBtn = view.findViewById(R.id.register_sign_up_btn);
        name=view.findViewById(R.id.register_user_name_et);
        password=view.findViewById(R.id.register_password_et);
        confirmPassword=view.findViewById(R.id.register_password_confirm_et);
        email=view.findViewById(R.id.register_user_email_et);
        progressBar=view.findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                User user=new User(name.getText().toString(),password.getText().toString(),email.getText().toString());
                Model.instance.getUsersList().add(user);
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_signInFragment);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }
}