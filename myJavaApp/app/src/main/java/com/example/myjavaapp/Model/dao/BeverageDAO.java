package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myjavaapp.Model.entity.Beverage;

import java.util.List;


@Dao
public interface BeverageDAO {
    @Query("SELECT * FROM Beverage")
    public LiveData<List<Beverage>> getAll();
    @Query("SELECT * FROM Beverage WHERE beverageId = :beverageId")
    public LiveData<Beverage> getBeverage(Integer beverageId);
    @Query("SELECT * FROM Beverage WHERE isBestSeller = 1")     // lấy danh sách các thức uống là best seller
    public LiveData<List<Beverage>> getBestSellerBeverage();
    @Query("SELECT * FROM BEVERAGE WHERE beverageType = :type")
    public LiveData<List<Beverage>> getBeveregaInType(String type);
    @Update
    public void updateBeverage(Beverage... beverages);
    @Query("SELECT * FROM Beverage WHERE beverageId = :id")
    public LiveData<Beverage> getBeverageFromId(String id);
    @Query("SELECT * FROM Beverage WHERE beverageName LIKE :key ")
    public LiveData<List<Beverage>> findBeverageWithKeyword(String key);

    @Query("UPDATE BEVERAGE SET beverageQuantitySeller = :quantity WHERE beverageId = :id")
    public void updateQuantitySeller(int quantity, String id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllBeverage(List<Beverage> beverages);
}
