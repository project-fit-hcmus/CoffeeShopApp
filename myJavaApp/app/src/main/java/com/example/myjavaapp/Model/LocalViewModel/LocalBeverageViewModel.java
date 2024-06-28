package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.repository.BeverageRepository;
import com.example.myjavaapp.Model.repository.CartRepository;

import java.util.List;

public class LocalBeverageViewModel extends AndroidViewModel {

    private final BeverageRepository mRepository;
    private LiveData<List<Beverage>> allBeverages;

    public LocalBeverageViewModel(Application application){
        super(application);
        this.mRepository = new BeverageRepository(application);
        allBeverages = mRepository.getAllBeverages();
    }
    public void insertAll(List<Beverage> beverages){
        mRepository.insertAll(beverages);
    }
    public void insert(Beverage beverage){
        mRepository.insert(beverage);
    }
    public LiveData<Beverage> getBeverageFromId(String id){
        return mRepository.getBeverageFromId(id);
    }
    public LiveData<List<Beverage>> getListBestSeller(){
        return mRepository.getListBestSeller();
    }
    public LiveData<List<Beverage>> getListBeverageWithType(String type){
        return mRepository.getListBeverageWithType(type);
    }
    public LiveData<List<Beverage>> findBeverageWithKeyword(String keyword){
        return mRepository.findBeverageWithKeyword(keyword);
    }

}
