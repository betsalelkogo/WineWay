package com.example.wineapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterFragment extends Fragment {
    EditText name, password, confirmPassword,email;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    User user=new User(name.getText().toString(),password.getText().toString(),email.getText().toString());
                                    Model.instance.addUser(user, ()->{
                                        Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_signInFragment);
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity(), task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
        setHasOptionsMenu(true);
        return view;
    }
}