package com.example.wineapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
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

import java.util.LinkedList;
import java.util.List;


public class UserAddPostFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    View view;
    EditText postEt, subjectEt;
    Button cancelBtn;
    ImageButton sendBtn, editPhoto;
    ImageView postPhoto;
    ProgressBar progressBar;
    User user;
    MapView map;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_add_post, container, false);
        user=UserAddPostFragmentArgs.fromBundle(getArguments()).getUser();
        sendBtn=view.findViewById(R.id.user_add_new_post_send_btn);
        cancelBtn=view.findViewById(R.id.user_add_new_post_cancel_btn);
        postEt=view.findViewById(R.id.user_add_new_post_et);
        subjectEt=view.findViewById(R.id.user_add_subject_post_et);
        progressBar=view.findViewById(R.id.user_add_new_post_progressbar);
        map=view.findViewById(R.id.user_add_new_post_map);
        editPhoto=view.findViewById(R.id.user_add_post_edit_image_btn);
        postPhoto=view.findViewById(R.id.user_add_new_post_photo_upload);
        progressBar.setVisibility(View.GONE);
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","editPhoto");
                EditImage();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                }});
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UserAddPostFragmentDirections.ActionUserAddPostFragmentToUserPageFragment action1 = UserAddPostFragmentDirections.actionUserAddPostFragmentToUserPageFragment(user);
                Navigation.findNavController(view).navigate(action1);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    private void EditImage() {
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap imageBitmap=(Bitmap) extras.get("data");
            postPhoto.setImageBitmap(imageBitmap);

        }
    }
    private void save() {
        Post p = new Post();
        progressBar.setVisibility(View.VISIBLE);
        sendBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        p.setName(user.getName());
        p.setSubject(subjectEt.getText().toString());
        p.setDetails(postEt.getText().toString());
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
                            Navigation.findNavController(sendBtn).navigateUp();
                        }
                    });
                }
            }});
//

    }
}