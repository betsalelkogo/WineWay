package com.example.wineapp.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    List<Post> getAll();

    @Query("SELECT * FROM Post WHERE details=:post_details")
    LiveData<List<Post>> getData(String post_details);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post posts);

    @Query("SELECT * FROM Post WHERE name=:name ")
    Post getPostByName(String name);
}
