package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.OrderDetailAndBeverageDAO;
import com.example.myjavaapp.Model.dao.OrderDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;

import java.util.List;

public class ODAndBERepository {

    private final OrderDetailAndBeverageDAO orderDetailAndBeverageDAO;
    private Application application;
    private LiveData<List<OrderDetailAndBeverage>> allItems;
    public ODAndBERepository(Application application){
        this.application = application;
        orderDetailAndBeverageDAO = AppDatabase.getDatabase(application).orderDetailAndBeverageDAO();
    }
    public LiveData<List<OrderDetailAndBeverage>> getAllItemsInOrder(String order){
        return orderDetailAndBeverageDAO.getAllBeverageInOrder(order);
    }
}
