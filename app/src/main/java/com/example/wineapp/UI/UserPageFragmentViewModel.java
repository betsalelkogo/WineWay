package com.example.wineapp.UI;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;

import java.util.List;

public class UserPageFragmentViewModel extends ViewModel {
    LiveData<List<Post>> data=Model.instance.getAll();
    User user;
    public LiveData<List<Post>> getData()
    {
        return data;
    }

    public void setUser(User user)
    {
        this.user = user;
        data=Model.instance.getPostByName(this.user.getName());
    }
}
