package com.example.wineapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.wineapp.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post implements Parcelable{
    public final static String LAST_UPDATED = "LAST_UPDATE";
    @PrimaryKey
    @NonNull
    private String id_key=null;
    private String subject;
    private String details;
    private String name;
    private String imageUrl;
    private double lang;
    private double lant;
    private boolean isDeleted;
    private Long lastUpdated = new Long(0);

    public Post(String name,String details,String subject,String imageUrl,double lang,double lant){
        this.name=name;
        this.details=details;
        this.subject=subject;
        this.imageUrl=imageUrl;
        this.lang=lang;
        this.lant=lant;
        this.isDeleted=false;
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
        isDeleted = in.readByte() != 0;
        if (in.readByte() == 0) {
            lastUpdated = null;
        } else {
            lastUpdated = in.readLong();
        }
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
        json.put("isDeleted", isDeleted());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
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
        boolean isDeleted = (boolean)json.get("isDeleted");
        Post p = new Post(name,details,subject,imageUrl,lang,lant);
        p.setDeleted(isDeleted);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        p.setLastUpdated(new Long(ts.getSeconds()));
        return p;
    }
    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("POSTS_LAST_UPDATE",0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("POSTS_LAST_UPDATE",date);
        editor.commit();
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_key);
        parcel.writeString(subject);
        parcel.writeString(details);
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeDouble(lang);
        parcel.writeDouble(lant);
        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        if (lastUpdated == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(lastUpdated);
        }
    }
}
