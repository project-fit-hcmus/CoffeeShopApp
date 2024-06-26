package com.example.myjavaapp.Model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myjavaapp.Model.converter.DateConverter;
import com.example.myjavaapp.Model.dao.BeverageDAO;
import com.example.myjavaapp.Model.dao.CartDAO;
import com.example.myjavaapp.Model.dao.CartDetailAndBeverageDAO;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Type;


@Database(entities = {Type.class, Beverage.class, Cart.class, CartDetail.class},
        version = 1,
        exportSchema = true // set true to be able to use automated migration in the future updates
        )
@TypeConverters(DateConverter.class)
public abstract  class AppDatabase extends RoomDatabase{
    private static AppDatabase sInstance;
    public static final String DATABASE_NAME = "basic-simple-database";
    public abstract TypeDAO typeDAO();
    public abstract BeverageDAO beverageDAO();
    public abstract CartDetailDAO cartDetailDAO();
    public abstract CartDAO cartDAO();
    public abstract CartDetailAndBeverageDAO cartDetailAndBeverageDAO();

    public static AppDatabase getDatabase(Context context){
        if(sInstance == null){
            synchronized (AppDatabase.class){
                if(sInstance == null){
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public static AppDatabase buildDatabase(Context context){
        return  Room.databaseBuilder(context,AppDatabase.class, DATABASE_NAME).build();
    }
}