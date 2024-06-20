package com.example.myjavaapp.View.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myjavaapp.View.Model.entity.Beverage;

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllBeverage(List<Beverage> beverages);
}
