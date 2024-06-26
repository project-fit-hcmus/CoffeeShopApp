package com.example.myjavaapp.Model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;


@Entity(tableName = "Cart")
public class Cart {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cartId")
    private String cartId;
    @ColumnInfo(name="cartUser")
    private String cartUser;
    public String getCartId(){return  this.cartId;}
    public String getCartUser(){return this.cartUser;}
    public void setCartId(String id){this.cartId = id;}
    public void setCartUser(String user){this.cartUser = user;}
    public Cart(String id, String user){
        this.cartId = id;
        this.cartUser = user;
    }
    public Cart(){}
}
