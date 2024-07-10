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
    @ColumnInfo(name = "orderDate")
    private String orderDate;
    @ColumnInfo(name = "orderOveralImage")
    private String orderOveralImage;
    @ColumnInfo(name = "isRating")
    private Boolean isRating;
    @ColumnInfo(name = "orderCover")
    private String orderCover;
    public String getOrderId(){return this.orderId;}
    public String getOrderUser(){return this.orderUser;}
    public String getOrderPhone(){return this.orderPhone;}
    public String getOrderNote(){return this.orderNote;}
    public String getOrderAddress(){return this.orderAddress;}
    public Integer getOrderCost(){return this.orderCost;}
    public String getOrderStatus(){return this.orderStatus;}
    public String getOrderDate(){return  this.orderDate;}
    public String getOrderOveralImage() { return this.orderOveralImage;}
    public Boolean getIsRating(){return this.isRating;}
    public String getOrderCover(){return this.orderCover;}
    public void setOrderId(String id){this.orderId = id;}
    public void setOrderUser(String id){this.orderUser = id;}
    public void setOrderPhone(String phone){this.orderPhone = phone;}
    public void setOrderNote(String note){this.orderNote = note;}
    public void setOrderAddress(String address){this.orderAddress = address;}
    public void setOrderCost(Integer cost){this.orderCost = cost;}
    public void setOrderStatus(String status){this.orderStatus = status;}
    public void setOrderDate(String date){this.orderDate = date;}
    public void setOrderOveralImage(String image){this.orderOveralImage = image;}
    public void setIsRating(Boolean rating){this.isRating = rating;}
    public void setOrderCover(String cover){this.orderCover = cover;}

    public Order(){}
    public Order(String id, String user, String phone, String note, String address, Integer cost, String status, String date, String image, Boolean rating, String cover){
        this.orderId = id;
        this.orderUser = user;
        this.orderPhone = phone;
        this.orderNote = note;
        this.orderAddress = address;
        this.orderCost = cost;
        this.orderStatus = status;
        this.orderDate = date;
        this.orderOveralImage = image;
        this.isRating = rating;
        this.orderCover = cover;
    }
}
