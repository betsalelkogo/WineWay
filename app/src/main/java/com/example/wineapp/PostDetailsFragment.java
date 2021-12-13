package com.example.wineapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

public class PostDetailsFragment extends Fragment {
    Post p;
    TextView caveName, details;
    ImageView photo;
    MapView map;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        p= PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        caveName=view.findViewById(R.id.post_details_cave_name_tv);
        details=view.findViewById(R.id.post_detail_tv);
        photo=view.findViewById(R.id.post_detail_wineryPicture);
        map=view.findViewById(R.id.post_details_map);
        //progressBar.setVisibility(View.VISIBLE);
        if (p != null){
            updateDisplay();
        }
        setHasOptionsMenu(true);
        return view;
    }
    private void updateDisplay() {
        caveName.setText(p.getName());
        details.setText(p.getDetails());
    }
}