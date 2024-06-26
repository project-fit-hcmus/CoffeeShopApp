package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.CartDetail;

import java.util.List;
import java.util.Map;

//not yet test
@Dao
public interface CartDetailAndBeverageDAO {
    @Transaction
//    @Query("SELECT CartDetail.*, Beverage.* FROM Beverage, Cart, CartDetail WHERE Beverage.beverageId = CartDetail.cartDetailBeverage AND Cart.cartId = CartDetail.cartDetailId AND Cart.cartUser = :id ")
//    public LiveData<List<Map<CartDetail, Beverage>>> getBeverageInCartDetail(String id);
    @Query("SELECT * FROM CartDetail WHERE cartDetailId = :cartId")
    public LiveData<List<BeverageAndCartDetail>> getBeverageInCartDetail(String cartId);
}


//references: https://developer.android.com/training/data-storage/room/accessing-data?hl=vi#multiple-tables