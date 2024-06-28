package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.OrderDetail;

@Dao
public interface OrderDetailDAO {
    @Query("SELECT OrderDetail.* FROM OrderDetail, `Order` WHERE orderId = orderDetailId AND orderUser = :user")
    public LiveData<OrderDetail> getAllOrderDetailOfUser(String user);
}
