package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.repository.CartRepository;

import java.util.List;

public class LocalCartViewModel extends AndroidViewModel {

    private final CartRepository mRepository;
    private LiveData<List<Cart>> allCarts;


    public LocalCartViewModel(Application application){
        super(application);
        this.mRepository = new CartRepository(application);
        allCarts = mRepository.getAllCarts();
    }

    public LiveData<List<Cart>> getAllCarts(){
        return this.allCarts;
    }
    public void insert(Cart cart){
        mRepository.insertCart(cart);
    }
    public void insertAllCarts(List<Cart> carts){
        mRepository.insertAllCart(carts);
    }

    public LiveData<String> getCartIdFromUser(String id){
        return mRepository.getCartIdFromUser(id);
    }

}
