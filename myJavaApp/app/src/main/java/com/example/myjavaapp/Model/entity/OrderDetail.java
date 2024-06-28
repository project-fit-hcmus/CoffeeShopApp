package com.example.myjavaapp.Model.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "OrderDetail", primaryKeys = {"orderDetailId","orderDetailBeverage"})
public class OrderDetail {
    @ColumnInfo(name = "orderDetailId")
    @NotNull
    private String orderDetailId;
    @ColumnInfo(name = "orderDetailBeverage")
    @NotNull
    private String orderDetailBeverage;
    @ColumnInfo(name="orderDetailQuantity")
    private Integer orderDetailQuantity;
    public String getOrderDetailId(){return this.orderDetailId;}
    public String getOrderDetailBeverage(){return this.orderDetailBeverage;}
    public Integer getOrderDetailQuantity(){return this.orderDetailQuantity;}
    public void setOrderDetailId(String id){this.orderDetailId = id;}
    public void setOrderDetailBeverage(String beverage){this.orderDetailBeverage = beverage;}
    public void setOrderDetailQuantity(Integer quantity){this.orderDetailQuantity = quantity;}

    public OrderDetail(){}
    public OrderDetail(String id, String beverage, Integer quantity){
        this.orderDetailId = id;
        this.orderDetailBeverage = beverage;
        this.orderDetailQuantity = quantity;
    }
}
