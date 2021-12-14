package com.example.wineapp.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wineapp.MyApplication;

@Database(entities = {Post.class,User.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract PostDao userDao();
}


public class AppLocalDB {
    static public final AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbPost.db")
                    .fallbackToDestructiveMigration()
                    .build();
    private AppLocalDB(){}
}