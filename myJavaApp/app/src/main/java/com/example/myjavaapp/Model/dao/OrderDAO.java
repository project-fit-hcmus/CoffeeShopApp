package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM `Order` WHERE orderUser = :userId")
    public LiveData<List<Order>> getAllOrder(String userId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Order> list);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Order item);

    @Query("SELECT * FROM `Order` WHERE orderUser = :user AND orderStatus = :status")
    public LiveData<List<Order>> getAllOrderBaseOnStatus(String user, String status);
    @Query("SELECT * FROM `Order` WHERE orderId = :id")
    public LiveData<Order> getOrer(String id);
    @Query("UPDATE `Order` SET isRating = :value WHERE orderId = :id")
    public void updateRatingForOrder(String id, boolean value);
}
