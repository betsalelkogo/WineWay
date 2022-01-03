package com.example.wineapp;

import android.app.MediaRouteButton;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;
import com.squareup.picasso.Picasso;


import java.util.LinkedList;
import java.util.List;


public class UserPageFragment extends Fragment {
    List<Post> alldata= new LinkedList<>();
    List<Post> data= null;
    View view;
    Adapter adapter;
    ProgressBar progressbar;
    TextView userName, email;
    User user;
    UserPageFragmentDirections.ActionUserPageFragmentToUserAddPostFragment action;
    SwipeRefreshLayout swipeRefresh;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_page, container, false);
        userName=view.findViewById(R.id.user_page_name_tv);
        email=view.findViewById(R.id.user_page_email_tv);
        progressbar= view.findViewById(R.id.user_page_progressbar);
        progressbar.setVisibility(View.VISIBLE);
        swipeRefresh=view.findViewById(R.id.user_page_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        user=UserPageFragmentArgs.fromBundle(getArguments()).getUser();
        RecyclerView list = view.findViewById(R.id.user_page_post_list_tv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                progressbar.setVisibility(View.VISIBLE);
                Post[] posts;
                posts=data.toArray(new Post[data.size()]);
                UserPageFragmentDirections.ActionUserPageFragmentToEditPostFragment action = UserPageFragmentDirections.actionUserPageFragmentToEditPostFragment(posts[position],user,position);
                Navigation.findNavController(v).navigate(action);
            }
        });

        userName.setText(user.getName());
        email.setText(user.getEmail());
        setHasOptionsMenu(true);
        action=UserPageFragmentDirections.actionUserPageFragmentToUserAddPostFragment(user);
        refreshData();
        return view;

    }
    private void refreshData() {
        Model.instance.getAllPosts(new Model.GetAllPostsListener(){
            @Override
            public void onComplete(List<Post> p) {
                alldata = p;
                adapter.notifyDataSetChanged();
                Filter();
                progressbar.setVisibility(View.GONE);
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }
    private void Filter(){
        data=new LinkedList<>();
        for(int i=0;i<alldata.size();i++) {
            if (user.getName().compareTo(alldata.get(i).getName())==0)
                data.add(alldata.get(i));
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_page_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result = true;
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.userPageLogout:
                    progressbar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(R.id.action_userPageFragment_to_startAppFragment);
                    break;
                case R.id.userPageAddPost:
                    progressbar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(action);
                    break;
                case R.id.userPageListPost:
                    progressbar.setVisibility(View.VISIBLE);
                    UserPageFragmentDirections.ActionUserPageFragmentToListPostFragment action1=UserPageFragmentDirections.actionUserPageFragmentToListPostFragment(user);
                    Navigation.findNavController(view).navigate(action1);
                    break;
                case R.id.userPageMapPost:
                    progressbar.setVisibility(View.VISIBLE);
                    UserPageFragmentDirections.ActionUserPageFragmentToMapFragment action12=UserPageFragmentDirections.actionUserPageFragmentToMapFragment(alldata.toArray(new Post[alldata.size()]),user);
                    Navigation.findNavController(view).navigate(action12);
                    break;
                default:
                    result = false;
                    break;
            }
        }
        return result;
    }
    class Adapter extends RecyclerView.Adapter<UserPageFragment.MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public UserPageFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_wine_list_row, parent, false);
            UserPageFragment.MyViewHolder holder = new UserPageFragment.MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserPageFragment.MyViewHolder holder, int position) {
            Post p = data.get(position);
            holder.nameTv.setText(p.getSubject());
            holder.detailsTv.setText(p.getDetails());
            holder.imageView.setImageResource(R.drawable.win);
            if(p.getImageUrl()!=null){
                Picasso.get().load(p.getImageUrl()).into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() { if(data==null) return 0;
            return data.size();}
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