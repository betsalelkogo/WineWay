package com.example.wineapp;

import android.Manifest;
import android.content.Intent;
import static android.app.Activity.RESULT_OK;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;


import java.util.List;

public class EditPostFragment extends Fragment implements OnMapReadyCallback {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    Post[] allpost;
    Post p;
    User user;
    View view;
    EditText postTextEd, subjectEt;
    MapView map;
    ProgressBar progressBar;
    ImageButton editPhoto;
    Button cancelBtn, deleteBtn,sendPostBtn;
    ImageView postPhoto;
    MarkerOptions[] marker;
    int position;
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
        allpost=EditPostFragmentArgs.fromBundle(getArguments()).getPost();
        user=EditPostFragmentArgs.fromBundle(getArguments()).getUser();
        position=EditPostFragmentArgs.fromBundle(getArguments()).getPosition();
        p=allpost[position];
        marker= new MarkerOptions[allpost.length];
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
                Model.instance.DeletePost(p, new Model.DeletePostListener() {
                    @Override
                    public void onComplete() {
                        EditPostFragmentDirections.ActionEditPostFragmentToUserPageFragment action1 = EditPostFragmentDirections.actionEditPostFragmentToUserPageFragment(user);
                        Navigation.findNavController(view).navigate(action1);
                    }
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
        postPhoto.setImageResource(R.drawable.win);
        if(p.getImageUrl()!=null){
            Picasso.get().load(p.getImageUrl()).into(postPhoto);
        }
        InitialGoogleMap(savedInstanceState);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap imageBitmap=(Bitmap) extras.get("data");
            postPhoto.setImageBitmap(imageBitmap);

        }
    }
    private void editPhoto() {
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        sendPostBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        p.setName(user.getName());
        p.setSubject(subjectEt.getText().toString());
        p.setDetails(postTextEd.getText().toString());
        BitmapDrawable bitmapDrawable=(BitmapDrawable)postPhoto.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        Model.instance.uploadImage(bitmap, p.getId_key(), new Model.UploadImageListener() {
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
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        for(int i=0;i<allpost.length;i++){
            marker[i]=new MarkerOptions().position(new LatLng(allpost[i].getLant(), allpost[i].getLang())).title(allpost[i].getSubject());
            map.addMarker(marker[i]);
        }
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                progressBar.setVisibility(View.VISIBLE);
                for(int i=0;i<allpost.length;i++){
                    if (marker.getTitle().equals(allpost[i].getSubject())){
                        updatePost(i);
                    }
                }
                return false;
            }
        });
    }
    private void updatePost(int i) {
        subjectEt.setText(allpost[i].getSubject());
        progressBar.setVisibility(View.GONE);
        postTextEd.setText(allpost[i].getDetails());
        postPhoto.setImageResource(R.drawable.win);
        if(allpost[i].getImageUrl()!=null){
            Picasso.get().load(allpost[i].getImageUrl()).into(postPhoto);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
    private void InitialGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        map.onCreate(mapViewBundle);

        map.getMapAsync(this);
    }
}