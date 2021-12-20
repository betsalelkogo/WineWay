package com.example.wineapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostDetailsFragment extends Fragment implements OnMapReadyCallback {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    Post p;
    TextView subjectEt, details;
    ImageView photo;
    MapView map;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        p= PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        subjectEt=view.findViewById(R.id.post_details_cave_name_tv);
        details=view.findViewById(R.id.post_detail_tv);
        photo=view.findViewById(R.id.post_detail_wineryPicture);
        map=view.findViewById(R.id.post_details_map);
        progressBar=view.findViewById(R.id.post_details_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        if (p != null){
            updateDisplay();
        }
        setHasOptionsMenu(true);
        InitialGoogleMap(savedInstanceState);
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
    private void updateDisplay() {
        progressBar.setVisibility(View.GONE);
        subjectEt.setText(p.getSubject());
        details.setText(p.getDetails());
        photo.setImageResource(R.drawable.win);
        if(p.getImageUrl()!=null){
            Picasso.get().load(p.getImageUrl()).into(photo);
        }
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
        map.addMarker(new MarkerOptions().position(new LatLng(Integer.parseInt(p.getLant()), Integer.parseInt(p.getLang()))).title("Marker"));
        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
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