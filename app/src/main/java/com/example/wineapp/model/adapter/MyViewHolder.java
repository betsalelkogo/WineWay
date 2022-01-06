package com.example.wineapp.model.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wineapp.OnItemClickListener;
import com.example.wineapp.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView nameTv;
    TextView detailsTv;
    ImageView imageView;

    public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.listrow_name_tv);
        detailsTv = itemView.findViewById(R.id.listrow_details_tv);
        imageView = itemView.findViewById(R.id.listrow_avatar_imv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                if (listener != null) {
                    listener.onItemClick(pos, v);
                }
            }
        });
    }
}
