package com.example.myjavaapp.View.Model.database;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.myjavaapp.View.Model.dao.BeverageDAO;
import com.example.myjavaapp.View.Model.dao.TypeDAO;
import com.example.myjavaapp.View.Model.entity.Beverage;
import com.example.myjavaapp.View.Model.entity.Type;
import com.example.myjavaapp.ViewModel.BeverageViewModel;
import com.example.myjavaapp.ViewModel.TypeViewModel;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HandleDataToRoom {
    private Context mainCtx;


    public HandleDataToRoom(Context context){
        this.mainCtx = context;

    }

    public void getAllBeverage(){
        BeverageViewModel beverageViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(BeverageViewModel.class);
        LiveData<DataSnapshot> liveData = beverageViewModel.getDataSnapshotLiveData();
        List<Beverage> temp = new ArrayList<>();
        BeverageDAO beverageDAO = AppDatabase.getDatabase(mainCtx).beverageDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                        temp.add(new Beverage(i.child("beverage_id").getValue(String.class), i.child("beverage_type").getValue(String.class),
                                i.child("beverage_image").getValue(String.class), i.child("beverage_name").getValue(String.class),
                                i.child("beverage_quantity_seller").getValue(Integer.class), i.child("beverage_rating").getValue(Double.class),
                                i.child("beverage_cost").getValue(String.class), i.child("is_best_seller").getValue(Boolean.class)));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            beverageDAO.insertAllBeverage(temp);
                        }
                    }).start();
                }
            }
        });
    }

    // not yet test
    public void getAllTypes(){
        TypeViewModel typeViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(TypeViewModel.class);
        LiveData<DataSnapshot> liveData = typeViewModel.getDataSnapshotLiveData();
        List<Type> temp = new ArrayList<>();
        TypeDAO typeDAO = AppDatabase.getDatabase(mainCtx).typeDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                   if(dataSnapshot != null){
                       for(DataSnapshot i : dataSnapshot.getChildren()){
                           temp.add(new Type(i.child("type_id").getValue(String.class), i.child("type_name").getValue(String.class),i.child("type_image").getValue(String.class)));
                       }
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               typeDAO.insertAllType(temp);
                           }
                       }).start();
                   }
            }
        });
    }

}
