package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCart(List<Cart> item);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertACart(Cart item);
    @Query("SELECT cartId FROM Cart WHERE cartUser = :user")
    public LiveData<String> getCartIdFromUser(String user);
    @Query("SELECT * FROM CartDetail, Cart WHERE cartDetailId = cartId AND cartUser = :user")
    public LiveData<CartDetail> getCartDetailOfUser(String user);
    @Query("SELECT * FROM Cart")
    public LiveData<List<Cart>> getAllCart();

}
