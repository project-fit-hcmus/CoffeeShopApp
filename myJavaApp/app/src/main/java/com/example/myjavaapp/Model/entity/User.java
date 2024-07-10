package com.example.myjavaapp.Model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "User")
public class User {
    @PrimaryKey
    @ColumnInfo(name = "userId")
    @NotNull
    private String userId;
    @ColumnInfo(name = "userName")
    private String userName;            // maybe not necessary
    @ColumnInfo(name = "userEmail")
    private String userEmail;
    @ColumnInfo(name = "userPhone"  )
    private String userPhone;
    @ColumnInfo(name = "userLocation")
    private String userLocation;
    @ColumnInfo(name = "userLatitude")
    private double userLatitude;
    @ColumnInfo(name = "userLongitude")
    private double userLongitude;
    //default constructor
    public User(){
    }
    public User(String id, String name, String mail, String phone, String location, double latitude, double longitude){
        this.userId = id;
        this.userName = name;
        this.userEmail = mail;
        this.userPhone = phone;
        this.userLocation = location;
        this.userLongitude = longitude;
        this.userLatitude = latitude;
    }

    public String getUserPhone(){
        return this.userPhone;
    }
    public String getUserId(){return  this.userId;}
    public String getUserName(){return  this.userName;}
    public String getUserEmail(){return  this.userEmail;}
    public String getUserLocation(){return  this.userLocation;}
    public double getUserLongitude(){ return this.userLongitude;  }
    public double getUserLatitude(){return this.userLatitude;}



    public void setUserId(String id){this.userId = id;}
    public void setUserName(String name){this.userName = name;}
    public void setUserPhone(String phone){
        this.userPhone = phone;
    }

    public void setUserEmail(String mail){this.userEmail = mail;}
    public void setUserLocation(String loc){ this.userLocation = loc;}
    public void setUserLongitude(double input){this.userLongitude = input;}
    public void setUserLatitude(double input){this.userLatitude = input;}

}
