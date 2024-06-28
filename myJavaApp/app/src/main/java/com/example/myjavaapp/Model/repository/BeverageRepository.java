package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.BeverageDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;

import java.util.List;

public class BeverageRepository {
    private final BeverageDAO beverageDAO;
    private Application application;
    private LiveData<List<Beverage>> allBeverages;
    public BeverageRepository(Application application){
        this.application = application;
        beverageDAO = AppDatabase.getDatabase(application.getApplicationContext()).beverageDAO();
        allBeverages = beverageDAO.getAll();
    }

    public LiveData<List<Beverage>> getAllBeverages(){
        return this.allBeverages;
    }

    public void insertAll(List<Beverage> beverages){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            beverageDAO.insertAllBeverage(beverages);
        } );
    }

    public void insert(Beverage beverage){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            beverageDAO.insertABeverage(beverage);
        });
    }
    public LiveData<Beverage> getBeverageFromId(String id){
        return beverageDAO.getBeverageFromId(id);
    }
    public LiveData<List<Beverage>> getListBestSeller(){
        return beverageDAO.getBestSellerBeverage();
    }
    public LiveData<List<Beverage>> getListBeverageWithType(String type){
        return  beverageDAO.getBeveregaInType(type);
    }
    public LiveData<List<Beverage>> findBeverageWithKeyword(String keyword){
        return beverageDAO.findBeverageWithKeyword(keyword);
    }



}
