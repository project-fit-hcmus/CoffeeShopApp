package com.example.myjavaapp.Model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myjavaapp.Model.converter.BeverageAndTypeConverter;
import com.example.myjavaapp.Model.converter.DateConverter;
import com.example.myjavaapp.Model.dao.BeverageDAO;
import com.example.myjavaapp.Model.dao.CartDAO;
import com.example.myjavaapp.Model.dao.CartDetailAndBeverageDAO;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.dao.FavoriteAndBeverageDAO;
import com.example.myjavaapp.Model.dao.FavoriteDAO;
import com.example.myjavaapp.Model.dao.OrderDAO;
import com.example.myjavaapp.Model.dao.OrderDetailAndBeverageDAO;
import com.example.myjavaapp.Model.dao.OrderDetailDAO;
import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Favorite;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.entity.Type;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Type.class, Beverage.class, Cart.class, CartDetail.class, Favorite.class, Order.class, OrderDetail.class},
        version = 1,
        exportSchema = true // set true to be able to use automated migration in the future updates
        )
@TypeConverters( BeverageAndTypeConverter.class)
public abstract  class AppDatabase extends RoomDatabase{
    private static volatile AppDatabase sInstance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static final String DATABASE_NAME = "basic-simple-database";
    public abstract TypeDAO typeDAO();
    public abstract BeverageDAO beverageDAO();
    public abstract CartDetailDAO cartDetailDAO();
    public abstract CartDAO cartDAO();
    public abstract CartDetailAndBeverageDAO cartDetailAndBeverageDAO();
    public abstract FavoriteDAO favoriteDAO();
    public abstract FavoriteAndBeverageDAO favoriteAndBeverageDAO();
    public abstract OrderDetailDAO orderDetailDAO();
    public abstract OrderDAO orderDAO();
    public abstract OrderDetailAndBeverageDAO orderDetailAndBeverageDAO();

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