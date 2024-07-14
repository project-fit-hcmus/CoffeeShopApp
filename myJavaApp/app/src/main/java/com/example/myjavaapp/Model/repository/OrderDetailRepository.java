package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.OrderDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetail;

import java.util.List;

public class OrderDetailRepository {

    private final OrderDetailDAO orderDetailDAO;
    private Application application;
    private LiveData<List<OrderDetail>> allDetails;
    public OrderDetailRepository(Application application){
        this.application = application;
        this.orderDetailDAO = AppDatabase.getDatabase(application).orderDetailDAO();
    }
    public LiveData<List<OrderDetail>> getAllDetailWithOrderId(String orderId){
        return orderDetailDAO.getAllDetailWithOrderId(orderId);
    }
    public void insertAll(List<OrderDetail> list){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDetailDAO.insertAll(list);
        });
    }
    public void insert(OrderDetail item){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDetailDAO.insert(item);
        });
    }
}
