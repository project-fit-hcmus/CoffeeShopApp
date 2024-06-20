package com.example.myjavaapp.View.Model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.protobuf.Internal;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "Beverage")
public class Beverage {
    @PrimaryKey
    @ColumnInfo(name = "beverageId")
    @NotNull
    private String beverageId;
    @ColumnInfo(name="beverageType")
    private String beverageType;
    @ColumnInfo(name="beverageName")
    private  String beverageName;
    @ColumnInfo(name="beverageImage")
    private String beverageImage;
    @ColumnInfo(name="beverageQuantitySeller")
    private Integer beverageQuantitySeller;
    @ColumnInfo(name="beverateRating")
    private double beverageRating;
    @ColumnInfo(name="isBestSeller")
    private  boolean isBestSeller;
    @ColumnInfo(name="beverageCost")
    private String beverageCost;
    public String getBeverageId(){return  this.beverageId;}
    public String getBeverageType(){return this.beverageType;}
    public String getBeverageName(){return  this.beverageName;}
    public String getBeverageImage(){return this.beverageImage;}
    public Integer getBeverageQuantitySeller(){return  this.beverageQuantitySeller;}
    public double getBeverageRating(){return  this.beverageRating;}
    public boolean getIsBestSeller(){return this.isBestSeller;}
    public String getBeverageCost(){return this.beverageCost;}
    public void setBeverageId(String id){this.beverageId = id;}
    public void setBeverageType(String type){this.beverageType = type;}
    public void setBeverageName(String name){this.beverageName = name;}
    public void setBeverageImage(String image){this.beverageImage = image;}
    public void setBeverageQuantitySeller(Integer quantity){this.beverageQuantitySeller = quantity;}
    public void setBeverageRating(double rating){this.beverageRating = rating;}
    public void setIsBestSeller(boolean check){this.isBestSeller = check;}
    public void setBeverageCost(String cost){this.beverageCost = cost;}
    public Beverage(String id, String type, String img, String name, Integer quantity, double rating, String cost, boolean bestseller){
        this.beverageId = id;
        this.beverageType = type;
        this.beverageImage = img;
        this.beverageName = name;
        this.beverageQuantitySeller = quantity;
        this.beverageRating = rating;
        this.beverageCost = cost;
        this.isBestSeller = bestseller;
    }
    public Beverage(){}

}

