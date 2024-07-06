package com.example.myjavaapp.Model.converter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.MatrixCursor;

import androidx.room.TypeConverter;

import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndType;
import com.example.myjavaapp.Model.entity.Type;

public class BeverageAndTypeConverter {
    @TypeConverter
    public BeverageAndType fromCursor(Cursor cursor){
        BeverageAndType beverageAndType = new BeverageAndType();
        if(cursor != null && cursor.moveToFirst()){
            @SuppressLint("Range") String TypeId = cursor.getString(cursor.getColumnIndex("TypeId"));
            @SuppressLint("Range") String TypeName = cursor.getString(cursor.getColumnIndex("TypeName"));
            @SuppressLint("Range") String TypeImage = cursor.getString(cursor.getColumnIndex("TypeImage"));

            Type type = new Type(TypeId, TypeName, TypeImage);

            @SuppressLint("Range") String beverageId = cursor.getString(cursor.getColumnIndex("beverageId"));
            @SuppressLint("Range") String beverageType = cursor.getString(cursor.getColumnIndex("beverageType"));
            @SuppressLint("Range") String beverageName = cursor.getString(cursor.getColumnIndex("beverageName"));
            @SuppressLint("Range") String beverageImage = cursor.getString(cursor.getColumnIndex("beverageImage"));
            @SuppressLint("Range") int beverageQuantitySeller = cursor.getInt(cursor.getColumnIndex("beverageQuantitySeller"));
            @SuppressLint("Range") double beverageRating = cursor.getDouble(cursor.getColumnIndex("beverageRating"));
            @SuppressLint("Range") int isBestSeller = cursor.getInt(cursor.getColumnIndex("isBestSeller"));
            boolean bestSeller;
            if(isBestSeller == 0)
                bestSeller = false;
            else bestSeller = true;


            @SuppressLint("Range") String beverageCost = cursor.getString(cursor.getColumnIndex(" beverageCost"));
            @SuppressLint("Range") String beverageDescription = cursor.getString(cursor.getColumnIndex("beverageDescription"));

            Beverage beverage = new Beverage(beverageId, beverageType, beverageName, beverageImage, beverageQuantitySeller, beverageRating,  beverageCost, bestSeller, beverageDescription);

            beverageAndType.beverage = beverage;
            beverageAndType.type = type;
        }
        return beverageAndType;
    }
    @TypeConverter
    public Cursor toCursor(BeverageAndType beverageAndType){
        MatrixCursor cursor = new MatrixCursor(new String[]{
            "TypeId","TypeName","TypeImage","beverageId","beverageType","beverageName","beverageImage","beverageQuantitySeller","beverageRating","beverageCost","isBestSeller","beverageDescription"
        });
        Object[] rowData = new Object[12];
        rowData[0] = beverageAndType.type.getTypeId();
        rowData[1] = beverageAndType.type.getTypeName();
        rowData[2] = beverageAndType.type.getTypeImage();
        rowData[3] = beverageAndType.beverage.getBeverageId();
        rowData[4] = beverageAndType.beverage.getBeverageType();
        rowData[5] = beverageAndType.beverage.getBeverageName();
        rowData[6] = beverageAndType.beverage.getBeverageImage();
        rowData[7] = beverageAndType.beverage.getBeverageQuantitySeller();
        rowData[8] = beverageAndType.beverage.getBeverageRating();
        rowData[9] = beverageAndType.beverage.getBeverageCost();
        rowData[10] = beverageAndType.beverage.getIsBestSeller();
        rowData[11] = beverageAndType.beverage.getBeverageDescription();
        cursor.addRow(rowData);
        return cursor;
    }
}
