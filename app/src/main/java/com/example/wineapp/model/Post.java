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
    private String id_key=null;
    private String subject;
    private String details;
    private String name;
    private String imageUrl;
    private double lang;
    private double lant;

    public Post(String name,String details,String subject,String imageUrl,double lang,double lant){
        this.name=name;
        this.details=details;
        this.subject=subject;
        this.imageUrl=imageUrl;
        this.lang=lang;
        this.lant=lant;
    }


    public Post() {
    }


    protected Post(Parcel in) {
        id_key = in.readString();
        subject = in.readString();
        details = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        lang = in.readDouble();
        lant = in.readDouble();
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

    public String getImageUrl(){return this.imageUrl;}

    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}

    public String getName(){return this.name;}

    public void setName(String name){this.name=name;}

    public String getId_key(){return this.id_key;}

    public void setId_key(String id_key){this.id_key=id_key;}

    public String getSubject(){return this.subject;}

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
        json.put("imageUrl", getImageUrl());
        json.put("lant", getLant());
        json.put("lang", getLang());
        return json;
    }



    static Post fromJson(Map<String,Object> json){
        String details = (String)json.get("details");
        if (details == null){
            return null;
        }
        String name = (String)json.get("name");
        String subject = (String)json.get("subject");
        String imageUrl = (String)json.get("imageUrl");
        double lang = (double)json.get("lang");
        double lant = (double)json.get("lant");
        Post p = new Post(name,details,subject,imageUrl,lang,lant);
        return p;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public double getLant() {
        return lant;
    }

    public void setLant(double lant) {
        this.lant = lant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_key);
        dest.writeString(subject);
        dest.writeString(details);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeDouble(lang);
        dest.writeDouble(lant);
    }
}
