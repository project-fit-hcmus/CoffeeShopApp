package com.example.myjavaapp.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartDetailViewModel extends ViewModel {

    private static final DatabaseReference HOT_STOCK_REF =
            FirebaseDatabase.getInstance().getReference("/cartdetails");
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(HOT_STOCK_REF);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}
