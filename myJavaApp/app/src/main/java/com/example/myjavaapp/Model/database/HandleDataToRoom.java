package com.example.myjavaapp.Model.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.myjavaapp.Model.LocalViewModel.LocalFavoriteViewModel;
import com.example.myjavaapp.Model.dao.BeverageDAO;
import com.example.myjavaapp.Model.dao.CartDAO;
import com.example.myjavaapp.Model.dao.CartDetailDAO;
import com.example.myjavaapp.Model.dao.FavoriteDAO;
import com.example.myjavaapp.Model.dao.OrderDAO;
import com.example.myjavaapp.Model.dao.OrderDetailDAO;
import com.example.myjavaapp.Model.dao.TypeDAO;
import com.example.myjavaapp.Model.entity.Beverage;
import com.example.myjavaapp.Model.entity.Cart;
import com.example.myjavaapp.Model.entity.CartDetail;
import com.example.myjavaapp.Model.entity.Favorite;
import com.example.myjavaapp.Model.entity.Order;
import com.example.myjavaapp.Model.entity.OrderDetail;
import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.ViewModel.BeverageViewModel;
import com.example.myjavaapp.ViewModel.CartDetailViewModel;
import com.example.myjavaapp.ViewModel.CartViewModel;
import com.example.myjavaapp.ViewModel.FavoriteViewModel;
import com.example.myjavaapp.ViewModel.OrderDetailViewModel;
import com.example.myjavaapp.ViewModel.OrderViewModel;
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
                        temp.add(new Beverage(i.child("beverageId").getValue(String.class), i.child("beverageType").getValue(String.class),
                                i.child("beverageImage").getValue(String.class), i.child("beverageName").getValue(String.class),
                                i.child("beverageQuantitySeller").getValue(Integer.class), i.child("beverageRating").getValue(Double.class),
                                i.child("beverageCost").getValue(String.class), i.child("isBestSeller").getValue(Boolean.class), i.child("beverageDescription").getValue(String.class)));
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
                           temp.add(new Type(i.child("typeId").getValue(String.class), i.child("typeName").getValue(String.class),i.child("typeImage").getValue(String.class)));
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

    // làm sao chỉ lấy ra các cartItem của user (Trường hợp firebase lưu nhiều user)
    public void getAllCartDetail(){
        Log.d("Start get Cartdetail","GET ALLL");
        CartDetailViewModel cartDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(CartDetailViewModel.class);
        LiveData<DataSnapshot> liveData = cartDetailViewModel.getDataSnapshotLiveData();
        List<CartDetail> temp = new ArrayList<>();
        CartDetailDAO cartDetailDAO = AppDatabase.getDatabase(mainCtx).cartDetailDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                            temp.add(new CartDetail(i.child("cartDetailId").getValue(String.class),i.child("cartDetailBeverage").getValue(String.class),i.child("cartDetailQuantity").getValue(Integer.class)));
                    }
                    Log.d("number of cart detail", String.valueOf(temp.size()));
                        new Thread(new Runnable() {
                            @Override
                            public void run(){cartDetailDAO.insertAllCartDetail(temp);
                            }
                        }).start();

                }
                liveData.removeObserver(this);
            }
        });
    }

    public void getAllCart(){
        CartViewModel cartViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(CartViewModel.class);
        LiveData<DataSnapshot> liveData = cartViewModel.getDataSnapshotLiveData();
        List<Cart> temp = new ArrayList<>();
        CartDAO cartDAO = AppDatabase.getDatabase(mainCtx).cartDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot i : dataSnapshot.getChildren()){
//                        if(i.child("cartUser").equals(id))
                            temp.add(new Cart(i.child("cartId").getValue(String.class), i.child("cartUser").getValue(String.class)));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cartDAO.insertAllCart(temp);
                        }
                    }).start();
                }
            }
        });
    }

    public void getAllFavoriteItems(String id){
        FavoriteViewModel favoriteViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(FavoriteViewModel.class);
        LiveData<DataSnapshot> liveData = favoriteViewModel.getDataSnapshotLiveData();
        List<Favorite> temp = new ArrayList<>();
        FavoriteDAO favoriteDAO = AppDatabase.getDatabase(mainCtx).favoriteDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                        if(i.child("favoriteUser").getValue(String.class).contains(id))
                            temp.add(new Favorite(i.child("favoriteUser").getValue(String.class), i.child("favoriteBeverage").getValue(String.class)));
                        Log.d("AFTER READ FAVORITE", String.valueOf(temp.size()));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            favoriteDAO.insertAllFavoriteItems(temp);
                        }
                    }).start();
                }
                liveData.removeObserver(this);
            }
        });
    }

    public void getAllOrder(String id){
        OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(OrderViewModel.class);
        LiveData<DataSnapshot> liveData = orderViewModel.getDataSnapshotLiveData();
        List<Order> temp = new ArrayList<>();
        OrderDAO orderDAO = AppDatabase.getDatabase(mainCtx).orderDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                        if(i.child("orderUser").getValue(String.class).contains(id))
                            temp.add(new Order(i.child("orderId").getValue(String.class),i.child("orderUser").getValue(String.class),i.child("orderPhone").getValue(String.class),i.child("orderNote").getValue(String.class), i.child("orderAddress").getValue(String.class),i.child("orderCost").getValue(Integer.class),i.child("orderStatus").getValue(String.class),i.child("orderDate").getValue(String.class),i.child("orderOveralImage").getValue(String.class)));
                        Log.d("AFTER READ FAVORITE", String.valueOf(temp.size()));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            orderDAO.insertAll(temp);
                        }
                    }).start();
                }
            }
        });
    }

    public void getAllOrderDetail(){
        OrderDetailViewModel orderDetailViewModel = new ViewModelProvider((ViewModelStoreOwner) mainCtx).get(OrderDetailViewModel.class);
        LiveData<DataSnapshot> liveData = orderDetailViewModel.getDataSnapshotLiveData();
        List<OrderDetail> temp = new ArrayList<>();
        OrderDetailDAO orderDetailDAO = AppDatabase.getDatabase(mainCtx).orderDetailDAO();
        liveData.observe((LifecycleOwner) mainCtx, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot i : dataSnapshot.getChildren()){
                        temp.add(new OrderDetail(i.child("orderDetailId").getValue(String.class),i.child("orderDetailBeverage").getValue(String.class),i.child("orderDetailQuantity").getValue(Integer.class)));
                        Log.d("AFTER READ FAVORITE", String.valueOf(temp.size()));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            orderDetailDAO.insertAll(temp);
                        }
                    }).start();
                }
            }
        });
    }
}
