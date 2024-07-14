package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDAO {
    @Query("SELECT OrderDetail.* FROM OrderDetail, `Order` WHERE orderId = orderDetailId AND orderUser = :user")
    public LiveData<OrderDetail> getAllOrderDetailOfUser(String user);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<OrderDetail> lists);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(OrderDetail item);
    @Query("SELECT * FROM OrderDetail WHERE orderDetailId = :orderId")
    public LiveData<List<OrderDetail>> getAllDetailWithOrderId(String orderId);

}
