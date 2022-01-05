package com.example.wineapp.UI;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wineapp.OnItemClickListener;
import com.example.wineapp.R;
import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ListPostFragment extends Fragment {
    ListPostFragmentViewModel viewModel;
    View view;
    MyAdapter adapter;
    User user;
    Post[] posts;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefresh;
    ListPostFragmentDirections.ActionListPostFragmentToUserPageFragment action1;
    ListPostFragmentDirections.ActionListPostFragmentToMapFragment action11;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ListPostFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_post, container, false);
        user=ListPostFragmentArgs.fromBundle(getArguments()).getUser();
        progressBar= view.findViewById(R.id.list_post_progressbar);
        progressBar.setVisibility(View.GONE);
        swipeRefresh=view.findViewById(R.id.winelist_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                Model.instance.reloadPostsList();
                swipeRefresh.setRefreshing(false);
            }
        });
        RecyclerView list = view.findViewById(R.id.winelist_list_rv);
        list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(linearLayoutManager);
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), linearLayoutManager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                progressBar.setVisibility(View.VISIBLE);
                Post p = viewModel.getData().getValue().get(position);
                ListPostFragmentDirections.ActionListPostFragmentToPostDetailsFragment action = ListPostFragmentDirections.actionListPostFragmentToPostDetailsFragment(p);
                Navigation.findNavController(v).navigate(action);
            }
        });
        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.notifyDataSetChanged();
            }
        });
        return view;
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
                    posts=viewModel.getData().getValue().toArray(new Post[viewModel.getData().getValue().size()]);
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
            Post p = viewModel.getData().getValue().get(position);
            holder.nameTv.setText(p.getSubject());
            holder.detailsTv.setText(p.getDetails());
            holder.imageView.setImageResource(R.drawable.win);
            if(p.getImageUrl()!=null){
                Picasso.get().load(p.getImageUrl()).into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) return 0;
            return viewModel.getData().getValue().size();
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