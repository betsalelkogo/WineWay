package com.example.wineapp;

import android.content.Intent;
import static android.app.Activity.RESULT_OK;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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
import com.google.android.gms.maps.MapView;

import java.util.List;

public class EditPostFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Post p;
    User user;
    View view;
    EditText postTextEd, subjectEt;
    MapView map;
    ProgressBar progressBar;
    ImageButton sendPostBtn,editPhoto;
    Button cancelBtn, deleteBtn;
    ImageView postPhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_edit_post, container, false);
        postTextEd=view.findViewById(R.id.edit_post_et);
        sendPostBtn=view.findViewById(R.id.edit_post_save_btn);
        editPhoto=view.findViewById(R.id.edit_post_image_btn);
        cancelBtn=view.findViewById(R.id.edit_post_cancel_btn);
        deleteBtn=view.findViewById(R.id.edit_post_delete_btn);
        progressBar=view.findViewById(R.id.edit_post_progressbar);
        map=view.findViewById(R.id.post_edit_map);
        subjectEt=view.findViewById(R.id.edit_post_subject_et);
        postPhoto=view.findViewById(R.id.edit_post_wineryPicture);
        progressBar.setVisibility(View.GONE);
        p=EditPostFragmentArgs.fromBundle(getArguments()).getPost();
        user=EditPostFragmentArgs.fromBundle(getArguments()).getUser();
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoto();
            }
        });
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
                save();
            }});
        setHasOptionsMenu(true);
        postTextEd.setText(p.getDetails());
        subjectEt.setText(p.getSubject());
        //postPhoto.setImageBitmap();
        return view;
    }
    private void editPhoto() {
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        Model.instance.DeletePost(p, () -> { });
        sendPostBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        p.setName(user.getName());
        p.setSubject(subjectEt.getText().toString());
        p.setDetails(postTextEd.getText().toString());
        BitmapDrawable bitmapDrawable=(BitmapDrawable)postPhoto.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        Model.instance.uploadImage(bitmap, user.getName(), new Model.UploadImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null) {

                } else {
                    p.setImageUrl(url);
                    Model.instance.addPost(p,new Model.AddPostListener(){
                        @Override
                        public void onComplete() {
                            EditPostFragmentDirections.ActionEditPostFragmentToUserPageFragment action1 = EditPostFragmentDirections.actionEditPostFragmentToUserPageFragment(user);
                            Navigation.findNavController(view).navigate(action1);
                        }
                    });
                }
            }});
    }
}