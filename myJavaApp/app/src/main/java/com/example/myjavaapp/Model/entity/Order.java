package com.example.myjavaapp.Model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Order")
public class Order {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="orderId")
    private String orderId;
    @NonNull
    @ColumnInfo(name="orderUser")
    private String orderUser;
    @ColumnInfo(name="orderPhone")
    private String orderPhone;
    @ColumnInfo(name="orderNote")
    private String orderNote;
    @ColumnInfo(name="orderAddress")
    private String orderAddress;
    @ColumnInfo(name="orderCost")
    private Integer orderCost;
    @ColumnInfo(name="orderStatus")
    private String orderStatus;
    public String getOrderId(){return this.orderId;}
    public String getOrderUser(){return this.orderUser;}
    public String getOrderPhone(){return this.orderPhone;}
    public String getOrderNote(){return this.orderNote;}
    public String getOrderAddress(){return this.orderAddress;}
    public Integer getOrderCost(){return this.orderCost;}
    public String getOrderStatus(){return this.orderStatus;}
    public void setOrderId(String id){this.orderId = id;}
    public void setOrderUser(String id){this.orderUser = id;}
    public void setOrderPhone(String phone){this.orderPhone = phone;}
    public void setOrderNote(String note){this.orderNote = note;}
    public void setOrderAddress(String address){this.orderAddress = address;}
    public void setOrderCost(Integer cost){this.orderCost = cost;}
    public void setOrderStatus(String status){this.orderStatus = status;}

    public Order(){}
    public Order(String id, String user, String phone, String note, String address, Integer cost, String status){
        this.orderId = id;
        this.orderUser = user;
        this.orderPhone = phone;
        this.orderNote = note;
        this.orderAddress = address;
        this.orderCost = cost;
        this.orderStatus = status;
    }
}
