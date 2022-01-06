package com.example.wineapp.UI;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wineapp.OnItemClickListener;
import com.example.wineapp.R;
import com.example.wineapp.model.Constants;
import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;
import com.example.wineapp.model.adapter.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class UserPageFragment extends Fragment {
    UserPageFragmentViewModel viewModel;
    View view;
    MyAdapter adapter;
    ProgressBar progressbar;
    TextView userName, email;
    User user;
    UserPageFragmentDirections.ActionUserPageFragmentToUserAddPostFragment action;
    SwipeRefreshLayout swipeRefresh;
    ImageButton photoUser;
    ImageView userImage;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserPageFragmentViewModel.class);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_page, container, false);
        userName=view.findViewById(R.id.user_page_name_tv);
        email=view.findViewById(R.id.user_page_email_tv);
        progressbar= view.findViewById(R.id.user_page_progressbar);
        progressbar.setVisibility(View.VISIBLE);
        swipeRefresh=view.findViewById(R.id.user_page_swipe_refresh);
        photoUser=view.findViewById(R.id.user_add_page_image_btn);
        userImage=view.findViewById(R.id.user_page_image);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoto();

            }
        });
        user=UserPageFragmentArgs.fromBundle(getArguments()).getUser();
        RecyclerView list = view.findViewById(R.id.user_page_post_list_tv);
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
                progressbar.setVisibility(View.VISIBLE);
                UserPageFragmentDirections.ActionUserPageFragmentToEditPostFragment action = UserPageFragmentDirections.actionUserPageFragmentToEditPostFragment(viewModel.getData().getValue().get(position),user,position);
                Navigation.findNavController(v).navigate(action);
            }
        });
        updateUserPage();
        setHasOptionsMenu(true);
        action=UserPageFragmentDirections.actionUserPageFragmentToUserAddPostFragment(user);
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.setFragment(UserPageFragment.this);
                Filter();
                adapter.setData(viewModel.getData().getValue());
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    private void updateUserPage() {
        userName.setText(user.getName());
        email.setText(user.getEmail());
        progressbar.setVisibility(View.GONE);
        userImage.setImageResource(R.drawable.userpage);
        if(user.getImageUrl().length()>2){
            Picasso.get().load(user.getImageUrl()).into(userImage);
        }
    }
    private void refreshData() {
        swipeRefresh.setRefreshing(true);
        Model.instance.reloadPostsList();
        Filter();
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
    private void Filter(){
        LiveData<List<Post>> data = viewModel.getData();
        for(int i=0;i<data.getValue().size();i++) {
            if (user.getName().compareTo(data.getValue().get(i).getName())!=0)
                viewModel.getData().getValue().remove(i);
        }
        viewModel.setData(data);
    }
    private void editPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userImage.setImageBitmap(imageBitmap);
            BitmapDrawable bitmapDrawable=(BitmapDrawable)userImage.getDrawable();
            Bitmap bitmap=bitmapDrawable.getBitmap();
            Model.instance.uploadImage(bitmap, user.getEmail(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if (url == null) {

                    } else {
                        user.setImageUrl(url);
                        Model.instance.addUser(user,()->{
                            return;
                        });
                    }
                }});
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
                    FirebaseAuth.getInstance().signOut();
                    Navigation.findNavController(view).navigate(R.id.action_userPageFragment_to_startAppFragment);
                    break;
                case R.id.userPageAddPost:
                    progressbar.setVisibility(View.VISIBLE);
                    Navigation.findNavController(view).navigate(action);
                    break;
                case R.id.userPageListPost:
                    viewModel=null;
                    progressbar.setVisibility(View.VISIBLE);
                    UserPageFragmentDirections.ActionUserPageFragmentToListPostFragment action1=UserPageFragmentDirections.actionUserPageFragmentToListPostFragment(user);
                    Navigation.findNavController(view).navigate(action1);
                    break;
                case R.id.userPageMapPost:
                    viewModel=null;
                    progressbar.setVisibility(View.VISIBLE);
                    UserPageFragmentDirections.ActionUserPageFragmentToMapFragment action12=UserPageFragmentDirections.actionUserPageFragmentToMapFragment(user);
                    Navigation.findNavController(view).navigate(action12);
                    break;
                default:
                    result = false;
                    break;
            }
        }
        return result;
    }
}