package com.example.wineapp.UI;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wineapp.model.Model;
import com.example.wineapp.model.Post;
import com.example.wineapp.model.User;

import java.util.List;

public class UserPageFragmentViewModel extends ViewModel {
    LiveData<List<Post>> data=Model.instance.getAll();
    public LiveData<List<Post>> getData()
    {
        return data;
    }

    public void setData(LiveData<List<Post>> data)
    {
        this.data=data;
    }
}
