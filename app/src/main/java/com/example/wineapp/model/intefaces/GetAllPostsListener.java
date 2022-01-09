package com.example.wineapp.model.intefaces;

import com.example.wineapp.model.Post;

import java.util.List;

public interface GetAllPostsListener {
    void onComplete(List<Post> data);
}
