package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.BeverageDAO;
import com.example.myjavaapp.Model.dao.CartDetailAndBeverageDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;

import java.util.List;

public class BeAndCartDetailRepository {
    private final CartDetailAndBeverageDAO cartDetailAndBeverageDAO;
    private Application application;
    public BeAndCartDetailRepository(Application application){
        this.application = application;
        cartDetailAndBeverageDAO = AppDatabase.getDatabase(application.getApplicationContext()).cartDetailAndBeverageDAO();
    }
    public LiveData<List<BeverageAndCartDetail>> getBeverageInCartDetail(String cartId){
        return cartDetailAndBeverageDAO.getBeverageInCartDetail(cartId);
    }
}
