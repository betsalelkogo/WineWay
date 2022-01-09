package com.example.wineapp.model.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wineapp.model.intefaces.OnItemClickListener;
import com.example.wineapp.R;
import com.example.wineapp.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    OnItemClickListener listener;
    private List<Post> data;
    private Fragment fragment;
    public MyAdapter() {
    }

    public MyAdapter(List<Post> data,Fragment fragment) {
        this.data=data;
        this.fragment=fragment;
    }
    public void setData(List<Post> data) {
        this.data=data;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = fragment.getLayoutInflater().inflate(R.layout.post_wine_list_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view, listener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post p = data.get(position);
        holder.nameTv.setText(p.getSubject());
        holder.detailsTv.setText(p.getDetails());
        holder.imageView.setImageResource(R.drawable.win);
        if (p.getImageUrl() != null) {
            Picasso.get().load(p.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        return data.size();
    }
}
