package com.example.wineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    MarkerOptions[] marker;
    View view;
    User user;
    Post[] allpost;
    MapFragmentDirections.ActionMapFragmentToUserPageFragment action1;
    MapFragmentDirections.ActionMapFragmentToListPostFragment action;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            }
            for(int i=0;i<allpost.length;i++){
                marker[i]=new MarkerOptions().position(new LatLng(allpost[i].getLant(), allpost[i].getLang())).title(allpost[i].getSubject());
                googleMap.addMarker(marker[i]);
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    for(int i=0;i<allpost.length;i++){
                        if (marker.getTitle().compareTo(allpost[i].getSubject())==0){
                            PostDetails(i);
                        }
                    }
                    return false;
                }
            });
            if(allpost.length>0){
                LatLng latlang = new LatLng(allpost[0].getLant(),allpost[0].getLang());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,7.5F));
            }
        }
    };

    private void PostDetails(int i) {
        MapFragmentDirections.ActionMapFragmentToPostDetailsFragment action=MapFragmentDirections.actionMapFragmentToPostDetailsFragment(allpost[i]);
        Navigation.findNavController(view).navigate(action);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_map, container, false);
        allpost=MapFragmentArgs.fromBundle(getArguments()).getListPost();
        user= MapFragmentArgs.fromBundle(getArguments()).getUser();
        marker= new MarkerOptions[allpost.length];
        setHasOptionsMenu(true);
        action1=MapFragmentDirections.actionMapFragmentToUserPageFragment(user);
        action=MapFragmentDirections.actionMapFragmentToListPostFragment(user);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view_gm);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_list_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result = true;
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.userPage:
                    //progressBar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(action1);
                    break;
                case R.id.post_list:
                    //progressBar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(action);
                    break;
                default:
                    result = false;
                    break;
            }
        }
        return result;
    }
}