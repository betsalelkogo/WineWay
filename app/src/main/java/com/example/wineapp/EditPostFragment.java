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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;

import java.util.List;

public class EditPostFragment extends Fragment {
    Post p;
    User user;
    View view;
    EditText postTextEd;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_edit_post, container, false);
        postTextEd=view.findViewById(R.id.edit_post_et);
        ImageButton sendPostBtn=view.findViewById(R.id.edit_post_save_btn);
        Button cancelBtn=view.findViewById(R.id.edit_post_cancel_btn);
        Button deleteBtn=view.findViewById(R.id.edit_post_delete_btn);
        progressBar=view.findViewById(R.id.edit_post_progressbar);
        progressBar.setVisibility(View.GONE);
        p=EditPostFragmentArgs.fromBundle(getArguments()).getPost();
        user=EditPostFragmentArgs.fromBundle(getArguments()).getUser();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                EditPostFragmentDirections.ActionEditPostFragmentToUserPageFragment action1 = EditPostFragmentDirections.actionEditPostFragmentToUserPageFragment(user);
                Navigation.findNavController(view).navigate(action1);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Model.instance.DeletePost(p,()->{
                    EditPostFragmentDirections.ActionEditPostFragmentToUserPageFragment action1 = EditPostFragmentDirections.actionEditPostFragmentToUserPageFragment(user);
                    Navigation.findNavController(view).navigate(action1);
                });
            }
        });
        sendPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                sendPostBtn.setEnabled(false);
                cancelBtn.setEnabled(false);
                p.setDetails(postTextEd.getText().toString());
                Model.instance.addPost(p, () -> {
                    EditPostFragmentDirections.ActionEditPostFragmentToUserPageFragment action1 = EditPostFragmentDirections.actionEditPostFragmentToUserPageFragment(user);
                    Navigation.findNavController(view).navigate(action1);
                });

            }});
        setHasOptionsMenu(true);
        postTextEd.setText(p.getDetails());
        return view;
    }
}