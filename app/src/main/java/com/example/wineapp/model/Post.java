package com.example.wineapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post implements Parcelable {
    @PrimaryKey
    @NonNull
    private String name = "";
    private String details="";
    public Post(String name,String details){
        this.name=name;
        this.details=details;
    }

    protected Post(Parcel in) {
        name = in.readString();
        details = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getName(){return this.name;}
    public void setName(String name){this.name=name;}
    public String getDetails() {
        return this.details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
    }
}
