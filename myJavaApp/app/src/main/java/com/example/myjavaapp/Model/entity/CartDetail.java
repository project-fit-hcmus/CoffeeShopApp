package com.example.myjavaapp.Model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "CartDetail", primaryKeys = {"cartDetailId","cartDetailBeverage"})
public class CartDetail {
    @ColumnInfo(name = "cartDetailId")
    @NonNull
    private String cartDetailId;
    @ColumnInfo(name = "cartDetailBeverage")
    @NotNull
    private String cartDetailBeverage;
    @ColumnInfo(name="cartDetailQuantity")
    private Integer cartDetailQuantity;
    public String getCartDetailId(){return this.cartDetailId;}
    public String getCartDetailBeverage(){return this.cartDetailBeverage;}
    public Integer getCartDetailQuantity(){return  this.cartDetailQuantity;}
    public void setCartDetailId(String id){this.cartDetailId = id;}
    public void setCartDetailBeverage(String id){this.cartDetailBeverage = id;}
    public void setCartDetailQuantity(Integer quantity){this.cartDetailQuantity = quantity;}

    public CartDetail(String cartId, String beverageId, Integer quantity){
        this.cartDetailId = cartId;
        this.cartDetailQuantity = quantity;
        this.cartDetailBeverage = beverageId;
    }
    public CartDetail(){}
}
