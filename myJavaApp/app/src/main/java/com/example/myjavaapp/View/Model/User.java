package com.example.myjavaapp.View.Model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

public class User {

    private String username;            // maybe not necessary
    private String email;
    private String phonenumber;
    private String location;
    private double latitude;
    private double longtitude;
    //default constructor
    public User(){

        username = null;
        email = null;
        phonenumber = null;
        location = null;
        latitude = 0f;
        longtitude = 0f;
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
    public double getLongtitude(){ return longtitude;  }
    public void setLongtitude(double input){this.longtitude = input;}
    public double getLatitude(){return latitude;}
    public void setLatitude(double input){this.latitude = input;}
}
