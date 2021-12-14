package com.example.wineapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;

import java.util.LinkedList;
import java.util.List;


public class UserAddPostFragment extends Fragment {
    View view;
    EditText post;
    Button cancelBtn;
    ImageButton sendBtn;
    int userPosition;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_add_post, container, false);
        user=UserAddPostFragmentArgs.fromBundle(getArguments()).getUser();
        sendBtn=view.findViewById(R.id.user_add_new_post_send_btn);
        cancelBtn=view.findViewById(R.id.user_add_new_post_cancel_btn);
        post=view.findViewById(R.id.user_add_new_post_et);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                }});
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAddPostFragmentDirections.ActionUserAddPostFragmentToUserPageFragment action1 = UserAddPostFragmentDirections.actionUserAddPostFragmentToUserPageFragment(user);
                Navigation.findNavController(view).navigate(action1);
            }
        });
        setHasOptionsMenu(true);
        return view;

    }
    private void save() {
        //progressbar.setVisibility(View.VISIBLE);
        sendBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        String new_post = post.getText().toString();
        Post p = new Post(user.getName(), new_post);
        Model.instance.addPost(p,()->{
            UserAddPostFragmentDirections.ActionUserAddPostFragmentToUserPageFragment action1 = UserAddPostFragmentDirections.actionUserAddPostFragmentToUserPageFragment(user);
            Navigation.findNavController(view).navigate(action1);
        });

    }
}