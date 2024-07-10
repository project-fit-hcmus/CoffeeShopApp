package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.UserDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.User;

import java.util.List;

public class UserRepository {
    private final UserDAO userDAO;
    private Application application;
    public UserRepository(Application application){
        this.application = application;
        userDAO = AppDatabase.getDatabase(application).userDAO();
    }
    public void insertAll(List<User> list){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insertAll(list);
        });
    }
    public void insert(User user){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.inset(user);
        });
    }
    public LiveData<User> getUserWithId(String id){
        return userDAO.getUserWithId(id);
    }
    public void update(User user){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.update(user);
        });
    }
}
