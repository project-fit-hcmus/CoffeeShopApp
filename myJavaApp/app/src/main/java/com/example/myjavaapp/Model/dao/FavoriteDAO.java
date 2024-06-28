package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.Favorite;

import java.util.List;

@Dao
public interface FavoriteDAO {
    @Delete
    abstract void deleteItemInFavorite(Favorite favorite);
    @Query("SELECT * FROM Favorite")
    public LiveData<List<Favorite>> getAllFavorite();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllFavoriteItems(List<Favorite> item);
    @Query("SELECT EXISTS (SELECT * FROM Favorite WHERE favoriteBeverage = :beverage)")
    public LiveData<Boolean> isExistBeverage(String beverage);
    @Query("SELECT * FROM Favorite WHERE favoriteBeverage = :beverageId ")
    public LiveData<Favorite> findFavoriteItemFromId(String beverageId);
    @Query("DELETE FROM Favorite WHERE favoriteBeverage = :id")
    public void deleteFavoriteById(String id);

    @Query("SELECT Beverage.* FROM Beverage, Favorite WHERE beverageId = favoriteBeverage")
    public LiveData<List<Beverage>> getAllFavoriteBeverage();
}
