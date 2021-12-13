package com.example.wineapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ListPostFragment extends Fragment {
    List<Post> data=new LinkedList<>();
    View view;
    MyAdapter adapter;
    int userPosition;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_post, container, false);
        userPosition=ListPostFragmentArgs.fromBundle(getArguments()).getUser();
        user=Model.instance.getUser(userPosition);
        Model.instance.getAllPosts(new Model.GetAllPostsListener(){
            @Override
            public void onComplete(List<Post> p) {
                data = p;
                adapter.notifyDataSetChanged();
                //progressbar.setVisibility(View.GONE);
            }
        });
        RecyclerView list = view.findViewById(R.id.winelist_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(this);
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //progressbar.setVisibility(View.VISIBLE);
                Post p = data.get(position);
                ListPostFragmentDirections.ActionListPostFragmentToPostDetailsFragment action = ListPostFragmentDirections.actionListPostFragmentToPostDetailsFragment(p);
                Navigation.findNavController(v).navigate(action);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.wine_list_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.userPageFragment:
                    ListPostFragmentDirections.ActionListPostFragmentToUserPageFragment action1=ListPostFragmentDirections.actionListPostFragmentToUserPageFragment(userPosition);
                    Navigation.findNavController(view).navigate(action1);
                    return true;
                default:
                    return false;
            }
        }
        return true;
    }


    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private final ListPostFragment listPostFragment;
        OnItemClickListener listener;

        public MyAdapter(ListPostFragment listPostFragment) {
            this.listPostFragment = listPostFragment;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = listPostFragment.getLayoutInflater().inflate(R.layout.post_wine_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post p = listPostFragment.data.get(position);
            holder.nameTv.setText(p.getName());
            holder.detailsTv.setText(p.getDetails());
        }

        @Override
        public int getItemCount() {
            return listPostFragment.data.size();
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView detailsTv;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.listrow_name_tv);
            detailsTv = itemView.findViewById(R.id.listrow_details_tv);
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