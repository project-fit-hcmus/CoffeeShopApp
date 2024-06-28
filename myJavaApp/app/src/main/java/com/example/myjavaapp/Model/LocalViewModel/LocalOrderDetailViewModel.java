package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.repository.OrderDetailRepository;
import com.example.myjavaapp.Model.repository.OrderRepository;

import java.util.List;

public class LocalOrderDetailViewModel extends AndroidViewModel {
    private OrderDetailRepository mRepository;
    private LiveData<List<OrderDetail>> allOrderDetails;
    public LocalOrderDetailViewModel(Application application){
        super(application);
        this.mRepository = new OrderDetailRepository(application);
    }
}
