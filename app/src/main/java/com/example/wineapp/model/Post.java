package com.example.wineapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post implements Parcelable{
    @PrimaryKey
    @NonNull
    private int id_key;
    private String subject;
    private String details;
    private String name;
    public static int counter=0;
    public Post(String name,String details,String subject){
        this.name=name;
        this.details=details;
        this.subject=subject;
        this.id_key=counter;
        counter++;
    }


    protected Post(Parcel in) {
        subject = in.readString();
        details = in.readString();
        name = in.readString();
        id_key = in.readInt();
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
    public int getId_key(){return this.id_key;}
    public void setId_key(int id_key){this.id_key=id_key;}
    public String getSubject(){return this.subject;}
    public void setName(String name){this.name=name;}
    public void setSubject(String subject){this.subject=subject;}
    public String getDetails() {
        return this.details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("name", getName());
        json.put("subject", getSubject());
        json.put("details", getDetails());
        return json;
    }

    static Post fromJson(Map<String,Object> json){
        String details = (String)json.get("details");
        if (details == null){
            return null;
        }
        String name = (String)json.get("name");
        String subject = (String)json.get("subject");
        Post p = new Post(name,details,subject);
        return p;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(details);
        dest.writeString(name);
        dest.writeInt(id_key);
    }
}
