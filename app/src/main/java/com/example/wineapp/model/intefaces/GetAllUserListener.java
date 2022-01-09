package com.example.wineapp.model.intefaces;

import com.example.wineapp.model.User;

import java.util.List;

public interface GetAllUserListener {
    void onComplete(List<User> data);
}
