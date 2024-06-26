package com.example.myjavaapp.Model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Favorite", primaryKeys = {"favorite_user","favorite_beverage"})
public class Favorite {
    @ColumnInfo(name = "favorite_user")
    private Integer favorite_user;
    @ColumnInfo(name = "favorite_beverage")
    private Integer favorite_beverage;
    public Integer getFavoriteUser(){return this.favorite_user;}
    public Integer getFavoriteBeverate(){return this.favorite_beverage;}
    public void setFavoriteUser(Integer user){this.favorite_user = user;}
    public void setFavoriteBeverage(Integer beverage){this.favorite_beverage = beverage;}
}
