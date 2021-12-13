package com.example.wineapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class User implements Parcelable {
    private String name;
    private String password;
    private String email;
    //private List<Post> list;
    public User(String name,String password,String email){
        this.name=name;
        this.password=password;
        this.email=email;
        //this.list=new LinkedList<Post>();
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
}
