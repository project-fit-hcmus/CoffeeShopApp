package com.example.myjavaapp.View.Model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Type")
public class Type {
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "TypeId")
    private String typeId;
    @ColumnInfo(name="TypeName")
    private String typeName;
    @ColumnInfo(name="TypeImage")
    private String typeImage;

    public Type(String id, String name, String img){
        this.typeId = id;
        this.typeName = name;
        this.typeImage = img;
    }
    public Type(){}
    public String getTypeId(){return this.typeId;}
    public String getTypeName(){return  this.typeName;}
    public String getTypeImage(){return this.typeImage;}
    public void setTypeId(String id){this.typeId = id;}
    public void setTypeName(String name){this.typeName = name;}
    public void setTypeImage(String image){this.typeImage = image;}
}
