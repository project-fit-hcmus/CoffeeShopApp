package com.example.myjavaapp.Model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Favorite", primaryKeys = {"favoriteUser","favoriteBeverage"})
public class Favorite {
    @ColumnInfo(name = "favoriteUser")
    @NonNull
    private String favoriteUser;
    @NonNull
    @ColumnInfo(name = "favoriteBeverage")
    private String favoriteBeverage;
    public String getFavoriteUser(){return this.favoriteUser;}
    public String getFavoriteBeverage(){return this.favoriteBeverage;}
    public void setFavoriteUser(String user){this.favoriteUser = user;}
    public void setFavoriteBeverage(String beverage){this.favoriteBeverage = beverage;}

    public Favorite(){}
    public Favorite(String user, String beverage){
        this.favoriteUser = user;
        this.favoriteBeverage = beverage;
    }
}
