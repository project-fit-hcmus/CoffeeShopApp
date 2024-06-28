package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.Model.repository.CateRepository;

import java.util.List;

public class LocalCateViewModel extends AndroidViewModel {
    private final CateRepository mRepository;
    private LiveData<List<Type>> allTypes;


    public LocalCateViewModel(Application application){
        super(application);
        this.mRepository = new CateRepository(application);
        allTypes = mRepository.getAllTypes();
    }

    public LiveData<List<Type>> getAllTypes(){
        return this.allTypes;
    }
    public void insert(Type type){
        mRepository.insertType(type);
    }
    public void insertAllTypes(List<Type> types){
        mRepository.insertAllTypes(types);
    }


}
