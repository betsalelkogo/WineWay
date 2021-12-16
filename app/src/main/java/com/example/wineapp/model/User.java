package com.example.wineapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User implements Parcelable {
    @PrimaryKey
    @NonNull
    private String name;
    private String password;
    private String email;
    public User(String name,String password,String email){
        this.name=name;
        this.password=password;
        this.email=email;
    }

    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName(){return this.name;}
    public void setName(String name){this.name=name;}
    public String getEmail() {
        return this.email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setEmail(String email) {
        this.email=email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(email);
    }
    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("name", getName());
        json.put("password", getPassword());
        json.put("email", getEmail());
        return json;
    }

    static User fromJson(Map<String,Object> json){
        String password = (String)json.get("password");
        if (password == null){
            return null;
        }
        String name = (String)json.get("name");
        String email = (String)json.get("email");
        User u = new User(name,password,email);
        return u;
    }
}
