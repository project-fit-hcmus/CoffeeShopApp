package com.example.myjavaapp.Model.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.myjavaapp.Model.dao.FavoriteDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndType;
import com.example.myjavaapp.Model.entity.Favorite;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteRepository {
    private final FavoriteDAO favoriteDAO;
    private Application application;
    private LiveData<List<Favorite>> allFavorites;
    public FavoriteRepository(Application application){
        this.application = application;
        favoriteDAO = AppDatabase.getDatabase(application).favoriteDAO();
        allFavorites = favoriteDAO.getAllFavorite();
    }

    public LiveData<List<Favorite>> getAllFavorites(){
        return this.allFavorites;
    }

    public void insertAllFavorite(List<Favorite> favorites){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDAO.insertAllFavoriteItems(favorites);
        });
    }
    public void insertAnItem(Favorite favorite){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDAO.insertAnItem(favorite);
        });
    }

    public void deleteAlbumById(String id){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                favoriteDAO.deleteFavoriteById(id);
            }
        });
    }

    public LiveData<List<Beverage>> getAllBeverageInFavorite(){
        return favoriteDAO.getAllFavoriteBeverage();
    }

    public LiveData<List<BeverageAndType>> getAllBeverageInFavoriteWithType(String UId){
        return favoriteDAO.getAllFavoriteBeverageWithType(UId);
    }
}
