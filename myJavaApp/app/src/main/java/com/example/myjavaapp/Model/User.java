package com.example.myjavaapp.Model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

public class User {

    private String username;
    private String email;
    private String phonenumber;
    private String location;
    //default constructor
    public User(){

        username = null;
        email = null;
        phonenumber = null;
        location = null;
    }

    public String getPhoneNumber(){
        return this.phonenumber;
    }
    public void setPhone(String phone){
        this.phonenumber = phone;
    }
    public String getUsername(){return  this.username;}
    public void setUsername(String name){this.username = name;}
    public String getEmail(){return  this.email;}
    public void setEmail(String mail){this.email = email;}
    public String getLocation(){return  this.location;}
    public void setLocation(String loc){ this.location = loc;}
}
