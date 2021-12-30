package com.example.wineapp;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import android.widget.Toast;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;


public class UserAddPostFragment extends Fragment implements OnMapReadyCallback {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    View view;
    EditText postEt, subjectEt;
    Button cancelBtn,sendBtn;
    ImageButton editPhoto;
    ImageView postPhoto;
    ProgressBar progressBar;
    User user;
    MapView map;
    LatLng lastKnownLocation=null;
    Post p = new Post();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_add_post, container, false);
        user = UserAddPostFragmentArgs.fromBundle(getArguments()).getUser();
        sendBtn = view.findViewById(R.id.user_add_new_post_send_btn);
        cancelBtn = view.findViewById(R.id.user_add_new_post_cancel_btn);
        postEt = view.findViewById(R.id.user_add_new_post_et);
        subjectEt = view.findViewById(R.id.user_add_subject_post_et);
        progressBar = view.findViewById(R.id.user_add_new_post_progressbar);
        map = view.findViewById(R.id.user_add_new_post_map);
        editPhoto = view.findViewById(R.id.user_add_post_edit_image_btn);
        postPhoto = view.findViewById(R.id.user_add_new_post_photo_upload);
        progressBar.setVisibility(View.GONE);
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoto();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()){
                    Toast.makeText(getActivity(), "Please check your input", Toast.LENGTH_SHORT).show();
                    return;
                }
                save();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UserAddPostFragmentDirections.ActionUserAddPostFragmentToUserPageFragment action1 = UserAddPostFragmentDirections.actionUserAddPostFragmentToUserPageFragment(user);
                Navigation.findNavController(view).navigate(action1);
            }
        });
        InitialGoogleMap(savedInstanceState);
        setHasOptionsMenu(true);

        return view;
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

    private void editPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            postPhoto.setImageBitmap(imageBitmap);

        }
    }
    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        sendBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        p.setName(user.getName());
        p.setSubject(subjectEt.getText().toString());
        p.setDetails(postEt.getText().toString());
        p.setLang(lastKnownLocation.longitude);
        p.setLant(lastKnownLocation.latitude);
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
                            Navigation.findNavController(sendBtn).navigateUp();
                        }
                    });
                }
            }});
    }
    private boolean validate() {
        return (subjectEt.getText().length() > 2 && postEt.getText().length() > 2&&lastKnownLocation!=null);
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
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            // TODO: Create interface for callback result
        }
        map.setMyLocationEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                lastKnownLocation=new LatLng(latLng.latitude,latLng.longitude);
                map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude,latLng.longitude)).title(p.getSubject()));
            }
        });
//        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        map.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())).title(p.getSubject()));
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
}