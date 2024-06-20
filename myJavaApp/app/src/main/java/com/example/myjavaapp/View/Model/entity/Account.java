package com.example.myjavaapp.View.Model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Account")
public class Account {
    @PrimaryKey
    private String user_id;
    @ColumnInfo(name="user_name")
    private String user_name;
    //constructor
    public Account(){

    }
    public Account(String id, String name){
        this.user_id = id;
        this.user_name = name;
    }

    public String getUserId(){return this.user_id;}
    public String getUserName(){return this.user_name;}
    public void setUserId(String input) { this.user_id = input;}
    public void setUserName(String name){ this.user_name = name;}
}
