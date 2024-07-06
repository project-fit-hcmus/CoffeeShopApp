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
//    @Query("SELECT CartDetail.*, Beverage.* FROM CartDetail, Beverage, Cart WHERE CartDetail.cartDetailId = Cart.cartId AND Cart.cartUser = :id AND CartDetail.cartDetailBeverage = Beverage.beverageId")
//    public LiveData<List<BeverageAndCartDetail>> getBeverageInCartDetail(String id);

//    @Transaction
    @Query("SELECT Beverage.*, CartDetail.*, Type.* FROM CartDetail, Beverage, Cart, Type WHERE Type.TypeId =Beverage.beverageType AND CartDetail.cartDetailId = Cart.cartId AND Cart.cartUser = :id AND CartDetail.cartDetailBeverage = Beverage.beverageId")
    public LiveData<List<BeverageAndCartDetail>> getBeverageInCartDetail(String id);
}


//references: https://developer.android.com/training/data-storage/room/accessing-data?hl=vi#multiple-tables