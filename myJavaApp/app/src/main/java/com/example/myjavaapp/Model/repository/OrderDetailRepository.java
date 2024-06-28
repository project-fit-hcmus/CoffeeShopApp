package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.OrderDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
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
}
