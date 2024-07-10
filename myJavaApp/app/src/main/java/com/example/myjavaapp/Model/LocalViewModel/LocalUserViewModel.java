package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.User;
import com.example.myjavaapp.Model.repository.OrderRepository;
import com.example.myjavaapp.Model.repository.UserRepository;

import java.util.List;

public class LocalUserViewModel extends AndroidViewModel {
    private UserRepository mRepository;
    private LiveData<List<User>> allUsers;
    public LocalUserViewModel(Application application){
        super(application);
        this.mRepository = new UserRepository(application);
    }
    public void insertAll(List<User> list){
        mRepository.insertAll(list);
    }
    public void insert(User user){
        mRepository.insert(user);
    }
    public LiveData<User> getUserWithId(String id){
        return mRepository.getUserWithId(id);
    }
    public void update(User user){
        mRepository.update(user);
    }
}
