package com.example.myjavaapp.View.Model;

public class Category {
    String name;
    Integer icon;

    public Category(String name, Integer icon){
        this.name = name;
        this.icon = icon;
    }
    public String getName(){ return this.name;}
    public Integer getIcon(){ return this.icon;}
    public void setName(String value){this.name = value;}
    public void setIcon(Integer value){this.icon = value;}
}
