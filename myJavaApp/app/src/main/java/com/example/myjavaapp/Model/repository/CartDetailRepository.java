package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.CartDAO;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartDetailRepository {
    private final CartDetailDAO cartDetailDAO;
    private Application application;
    private LiveData<List<CartDetail>> allCartDetails;
    public CartDetailRepository(Application application){
        this.application = application;
        cartDetailDAO = AppDatabase.getDatabase(application.getApplicationContext()).cartDetailDAO();
        allCartDetails = cartDetailDAO.getAllItem();
    }

//lấy toàn bộ item của cartdetail
    public LiveData<List<CartDetail>> getAllItems(){
        return this.allCartDetails;
    }


    // lấy toàn bộ item của cartdetail thuộc cartid
    public LiveData<List<CartDetail>> getAllDetailItemInCart(String cart){
        return  cartDetailDAO.getDetailItemInCart(cart);
    }

    public LiveData<Boolean> BeverageIsExistInCart(String beverage, String cartId){
        return cartDetailDAO.isExistBeverage(beverage, cartId);
    }

    //the same of above (TEST)
    public LiveData<Integer> checkIfBeverageIsExist(String beverage, String id){
        return cartDetailDAO.checkIfBeverageIsExist(beverage,id);
    }

    public LiveData<Integer> getQuantityOfItemInCartDetail(String beverage, String cartId){
        return cartDetailDAO.getQuantityOfCartDetail(cartId,beverage);
    }

    // update/ insert / delete
    public void insertAll(List<CartDetail> carts){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDetailDAO.insertAllCartDetail(carts);
        } );
    }

    public void insertAnItem(CartDetail item){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDetailDAO.insertOneCartDetail(item);
        });
    }

    //check kỹ ham này
    public void deleteItemInCartDetail(String beverage, String cartId){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDetailDAO.deleteBeverageInCartDetail(beverage,cartId);
        });
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                cartDetailDAO.deleteBeverageInCartDetail(beverage,cartId);
//            }
//        });
    }

    public void DeleteItemInCartDetail(CartDetail item){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDetailDAO.DeleteItemInCartDetail(item);
        });
    }

}
