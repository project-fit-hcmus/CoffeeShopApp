package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.repository.OrderRepository;

import java.util.List;

public class LocalOrderViewModel extends AndroidViewModel {
        private OrderRepository mRepository;
        private LiveData<List<Order>> allOrders;
        public LocalOrderViewModel(Application application){
            super(application);
            this.mRepository = new OrderRepository(application);
        }
        public LiveData<List<Order>> getAllOrders(String UserId){
            return mRepository.getAllOrders(UserId);
        }
    public LiveData<List<Order>> getAllOrderBaseOnStatus(String user, String status){
            return mRepository.getAllOrderBaseOnStatus(user,status);
    }
    public LiveData<Order> getOrderItem(String id){
            return mRepository.getOrderItem(id);
    }
}
