package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.entity.OrderDetailAndBeverage;
import com.example.myjavaapp.Model.repository.ODAndBERepository;
import com.example.myjavaapp.Model.repository.OrderDetailRepository;

import java.util.List;

public class LocalODAndBEViewModel extends AndroidViewModel {
    private ODAndBERepository mRepository;
    private LiveData<List<OrderDetailAndBeverage>> alItems;
    public LocalODAndBEViewModel(Application application){
        super(application);
        mRepository = new ODAndBERepository(application);
    }
}
