package com.example.wineapp.model;


import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PostDao {
    @Query("select * from Post")
    List<Post> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post posts);

    @Query("SELECT * FROM Post WHERE name=:name ")
    Post getPostByName(String name);
}
