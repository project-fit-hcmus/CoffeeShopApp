package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myjavaapp.Model.entity.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<User> list);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void inset(User user);
    @Query("SELECT * FROM User")
    public LiveData<List<User>> getAllUser();
    @Query("SELECT * FROM User WHERE userId = :id")
    public LiveData<User> getUserWithId(String id);
    //CHƯA TEST
    @Query("UPDATE User SET userLatitude = :latitude AND userLongitude = :longitude AND userLocation = :location AND userPhone = :phone WHERE userId = :id")
    public void updateSomeInfo(Double latitude, Double longitude, String location, String phone, String id);
    @Update
    public void update(User user);
}
