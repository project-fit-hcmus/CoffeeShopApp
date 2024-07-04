package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;

import java.util.List;

@Dao
public interface OrderDetailAndBeverageDAO {
    @Query("SELECT OrderDetail.*, Beverage.* FROM OrderDetail, Beverage WHERE Beverage.beverageId = OrderDetail.orderDetailBeverage AND OrderDetail.orderDetailId = :orderId")
    LiveData<List<OrderDetailAndBeverage>> getAllBeverageInOrder(String orderId);

}
