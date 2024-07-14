package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.FavoriteDAO;
import com.example.myjavaapp.Model.dao.OrderDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Favorite;
import com.example.myjavaapp.Model.entity.Order;

import java.util.List;

public class OrderRepository {
    private final OrderDAO orderDAO;
    private Application application;
    private LiveData<List<Order>> allOrders;
    public OrderRepository(Application application){
        this.application = application;
        orderDAO = AppDatabase.getDatabase(application).orderDAO();
    }

    public LiveData<List<Order>> getAllOrders(String Uid){
        allOrders = orderDAO.getAllOrder(Uid);
        return allOrders;
    }

    public LiveData<List<Order>> getAllOrderBaseOnStatus(String user, String status){
        return orderDAO.getAllOrderBaseOnStatus(user,status);
    }

    public LiveData<Order> getOrderItem(String id){
        return orderDAO.getOrer(id);
    }
    public void InsertAll(List<Order> list){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            orderDAO.insertAll(list);
        });
    }
    public void Insert(Order item){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                orderDAO.insert(item);
            });
        });
    }
}
