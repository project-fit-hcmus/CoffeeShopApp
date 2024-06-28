package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.CartDAO;
import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Type;

import java.util.List;

public class CartRepository {
    private final CartDAO cartDAO;
    private Application application;
    private LiveData<List<Cart>> allCarts;
    public CartRepository(Application application){
        this.application = application;
        cartDAO = AppDatabase.getDatabase(application.getApplicationContext()).cartDAO();
        allCarts = cartDAO.getAllCart();
    }

    public LiveData<List<Cart>> getAllCarts(){
        return this.allCarts;
    }

    public void insertAllCart(List<Cart> carts){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDAO.insertAllCart(carts);
        } );
    }

    public void insertCart(Cart cart){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDAO.insertACart(cart);
        });
    }

    public LiveData<String> getCartIdFromUser(String id){
        return cartDAO.getCartIdFromUser(id);
    }

}
