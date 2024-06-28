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
        allOrders = mRepository.getAllOrders();
    }
    public LiveData<List<Order>> getAllOrders(){
        return mRepository.getAllOrders();
    }
}
