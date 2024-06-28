package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM `Order`")
    public LiveData<List<Order>> getAllOrder();
}
