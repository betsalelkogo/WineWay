package com.example.wineapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ListPostFragment extends Fragment {
    List<Post> data=new LinkedList<>();
    View view;
    MyAdapter adapter;
    User user;
    Post[] posts;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefresh;
    ListPostFragmentDirections.ActionListPostFragmentToUserPageFragment action1;
    ListPostFragmentDirections.ActionListPostFragmentToMapFragment action11;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_post, container, false);
        user=ListPostFragmentArgs.fromBundle(getArguments()).getUser();
        progressBar= view.findViewById(R.id.list_post_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        swipeRefresh=view.findViewById(R.id.winelist_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });


        RecyclerView list = view.findViewById(R.id.winelist_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                progressBar.setVisibility(View.VISIBLE);
                Post p = data.get(position);
                ListPostFragmentDirections.ActionListPostFragmentToPostDetailsFragment action = ListPostFragmentDirections.actionListPostFragmentToPostDetailsFragment(p);
                Navigation.findNavController(v).navigate(action);
            }
        });
        setHasOptionsMenu(true);
        refreshData();
        return view;
    }
    private void refreshData() {
        Model.instance.getAllPosts(new Model.GetAllPostsListener(){
            @Override
            public void onComplete(List<Post> p) {
                data = p;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.wine_list_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result = true;
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.userPage:
                    action1=ListPostFragmentDirections.actionListPostFragmentToUserPageFragment(user);
                    progressBar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(action1);
                    break;
                case R.id.map_list:
                    posts=data.toArray(new Post[data.size()]);
                    action11=ListPostFragmentDirections.actionListPostFragmentToMapFragment(posts,user);
                    progressBar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(action11);
                    break;
                default:
                    result = false;
                    break;
            }
        }
        return result;
    }
     class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

         public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_wine_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post p = data.get(position);
            holder.nameTv.setText(p.getSubject());
            holder.detailsTv.setText(p.getDetails());
            holder.imageView.setImageResource(R.drawable.win);
            if(p.getImageUrl()!=null){
                Picasso.get().load(p.getImageUrl()).into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView detailsTv;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.listrow_name_tv);
            detailsTv = itemView.findViewById(R.id.listrow_details_tv);
            imageView=itemView.findViewById(R.id.listrow_avatar_imv);
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
}