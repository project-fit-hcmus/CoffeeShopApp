package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myjavaapp.Model.entity.FavoriteAndBeverage;

import java.util.List;

@Dao
public interface FavoriteAndBeverageDAO {
    @Transaction
    @Query("SELECT * FROM Favorite WHERE favoriteUser = :Uid ")
    public LiveData<List<FavoriteAndBeverage>> getFavoriteBeverage(String Uid);
}
