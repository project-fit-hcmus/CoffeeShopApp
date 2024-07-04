package com.example.myjavaapp.Model.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Database;

import com.example.myjavaapp.Model.dao.CartDAO;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
//        AppDatabase.databaseWriteExecutor.execute(() -> {
//            cartDetailDAO.deleteBeverageInCartDetail(beverage,cartId);
//        });
//        ------------
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                cartDetailDAO.deleteBeverageInCartDetail(beverage,cartId);
//            }
//        });
        Toast.makeText(application.getApplicationContext(),"go into delete",Toast.LENGTH_SHORT).show();
        new DeleteItemTask(cartDetailDAO, beverage, cartId).execute();
        Log.d("delete in repository","finish");
        Toast.makeText(application.getApplicationContext(),"on delete",Toast.LENGTH_SHORT).show();


    }

    public void DeleteItemInCartDetail(CartDetail item){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDetailDAO.DeleteItemInCartDetail(item);
        });
    }
    public void UpdateQuantityOfAnItem(Integer quantity, String cartId, String beverage){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            cartDetailDAO.updateQuantityBeverage(quantity,cartId,beverage);
        });
    }
    public void UpdateAnItem(CartDetail item){
        AppDatabase.databaseWriteExecutor.execute(()->{
            cartDetailDAO.updateAnItem(item);
        });
    }

//    public void updateInRealtime(){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cartdetails");
//       getAllItems().observe((LifecycleOwner) application.getApplicationContext(), new Observer<List<CartDetail>>() {
//           @Override
//           public void onChanged(List<CartDetail> cartDetails) {
//               if(cartDetails != null && ca)
//           }
//       });
//
//    }

    private static class DeleteItemTask extends AsyncTask<Void, Void, Void>{
        private CartDetailDAO cartDetailDAO;
        private String beverageId;
        private String cartId;
        public DeleteItemTask(CartDetailDAO dao, String beverage, String cart){
            this.cartDetailDAO = dao;
            this.beverageId = beverage;
            this.cartId = cart;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            cartDetailDAO.deleteBeverageInCartDetail(beverageId,cartId);
            Log.d("delete in async task","success");
            return null;
        }
    }

}
