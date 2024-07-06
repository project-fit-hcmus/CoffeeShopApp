package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.BeverageAndType;
import com.example.myjavaapp.Model.entity.Favorite;
import com.example.myjavaapp.Model.repository.FavoriteRepository;
import com.facebook.internal.AppCall;

import java.util.List;

public class LocalFavoriteViewModel extends AndroidViewModel {
    private FavoriteRepository mRepository;
    private LiveData<List<Favorite>> allFavorites;
    public LocalFavoriteViewModel(Application application){
        super(application);
        mRepository = new FavoriteRepository(application);
        allFavorites = mRepository.getAllFavorites();
    }

    public LiveData<List<Favorite>> getAllFavorites(){
        return allFavorites;
    }
    public void insert(List<Favorite> favorites){
        mRepository.insertAllFavorite(favorites);
    }

    public void insertAnItem(Favorite favorite){
        mRepository.insertAnItem(favorite);
    }

    public void deleteFavoriteById(String id){
        mRepository.deleteAlbumById(id);
    }
    public LiveData<List<Beverage>> getAllBeverageFromFavorite(){
        return mRepository.getAllBeverageInFavorite();
    }

    public LiveData<List<BeverageAndType>> getAllBeverageFromFavoriteWithType(String UId){
        return mRepository.getAllBeverageInFavoriteWithType(UId);
    }
}
