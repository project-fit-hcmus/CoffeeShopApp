package com.example.myjavaapp.View.Model.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myjavaapp.View.Model.converter.DateConverter;
import com.example.myjavaapp.View.Model.dao.BeverageDAO;
import com.example.myjavaapp.View.Model.dao.TypeDAO;
import com.example.myjavaapp.View.Model.entity.Beverage;
import com.example.myjavaapp.View.Model.entity.Type;

import java.util.List;

@Database(entities = {Type.class, Beverage.class},
        version = 1,
        exportSchema = true // set true to be able to use automated migration in the future updates
        )
@TypeConverters(DateConverter.class)
public abstract  class AppDatabase extends RoomDatabase{
    private static AppDatabase sInstance;
    public static final String DATABASE_NAME = "basic-simple-database";
    public abstract TypeDAO typeDAO();
    public abstract BeverageDAO beverageDAO();

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