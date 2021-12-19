package com.example.wineapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostDetailsFragment extends Fragment {
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
        return view;
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
}