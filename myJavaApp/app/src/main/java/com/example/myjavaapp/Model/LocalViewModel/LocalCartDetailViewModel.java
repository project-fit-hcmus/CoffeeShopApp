package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.repository.CartDetailRepository;
import java.util.List;

public class LocalCartDetailViewModel extends AndroidViewModel {
    private final CartDetailRepository mRepository;
    private LiveData<List<CartDetail>> allCartDetails;


    public LocalCartDetailViewModel(Application application){
        super(application);
        this.mRepository = new CartDetailRepository(application);
        allCartDetails = mRepository.getAllItems();
    }

    public LiveData<List<CartDetail>> getAllItems(){
        return this.allCartDetails;
    }
    public void insertAll(List<CartDetail> cartDetails){
        mRepository.insertAll(cartDetails);
    }

    public void insert(CartDetail cartDetail){
        mRepository.insertAnItem(cartDetail);
    }
    public LiveData<List<CartDetail>> getAllDetailItemInCart(String cartId){
        return mRepository.getAllDetailItemInCart(cartId);
    }
    public LiveData<Boolean> beverageIsExistsInCart(String beverageId, String cartId){
        return  mRepository.BeverageIsExistInCart(beverageId,cartId);
    }
    public LiveData<Integer> checkIfBeverageIsExist(String beverage, String id){
        return  mRepository.checkIfBeverageIsExist(beverage,id);
    }
    public LiveData<Integer> getQuantityOfItemInCartDetail(String beverageId, String cartId){
        return mRepository.getQuantityOfItemInCartDetail(beverageId,cartId);
    }
    public void deleteItemInCartDetail(String beverage, String id){
        mRepository.deleteItemInCartDetail(beverage, id);
    }
    public void DeleteItemInCartDetail(CartDetail item){
        mRepository.DeleteItemInCartDetail(item);
    }
}
