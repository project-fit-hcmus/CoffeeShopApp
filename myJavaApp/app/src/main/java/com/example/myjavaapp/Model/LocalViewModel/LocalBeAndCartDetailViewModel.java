package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.repository.BeAndCartDetailRepository;
import com.example.myjavaapp.Model.repository.CartDetailRepository;

import java.util.List;

import kotlin.jvm.internal.LocalVariableReference;

public class LocalBeAndCartDetailViewModel extends AndroidViewModel {
    private final BeAndCartDetailRepository mRepository;



    public LocalBeAndCartDetailViewModel(Application application){
        super(application);
        this.mRepository = new BeAndCartDetailRepository(application);
    }
    public LiveData<List<BeverageAndCartDetail>> getBeverageInCartDetail(String cartId){
        return mRepository.getBeverageInCartDetail(cartId);
    }
}
