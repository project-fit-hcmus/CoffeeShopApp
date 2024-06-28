package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Type;

import java.util.List;

public class CateRepository {
    private final TypeDAO typeDAO;
    private Application application;
    private LiveData<List<Type>> types;
    public CateRepository(Application application){
        this.application = application;
        typeDAO = AppDatabase.getDatabase(application.getApplicationContext()).typeDAO();
        types = typeDAO.getAllType();
    }

    public LiveData<List<Type>> getAllTypes(){
        return this.types;
    }

    public void insertAllTypes(List<Type> types){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            typeDAO.insertAllType(types);
        } );
    }

    public void insertType(Type type){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            typeDAO.insertType(type);
        });
    }
    public LiveData<String> getNameTypeFromId(String id){
        return this.typeDAO.getNameFromId(id);
    }


}
