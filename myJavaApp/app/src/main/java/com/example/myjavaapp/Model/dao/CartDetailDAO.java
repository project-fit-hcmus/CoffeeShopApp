package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.CartDetail;

import java.util.List;

@Dao
public interface CartDetailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCartDetail(List<CartDetail> detailItems);
    @Insert
    public void insertOneCartDetail(CartDetail item);
    @Query("SELECT * FROM CARTDETAIL WHERE cartDetailId = :cartId")
    public LiveData<List<CartDetail>> getDetailItemInCart(String cartId);

    @Query("SELECT EXISTS (SELECT * FROM CartDetail WHERE cartDetailBeverage = :beverage AND cartDetailId = :id)")
    public LiveData<Boolean> isExistBeverage(String beverage, String id);

//    TEST
    @Query("SELECT cartDetailQuantity FROM CartDetail WHERE  CartDetail.cartDetailBeverage = :beverage AND cartDetailId = :id")
    public LiveData<Integer> checkIfBeverageIsExist(String beverage, String id);
//    TEST
    @Query("SELECT * FROM CartDetail")
    public LiveData<List<CartDetail>> getAllItem();

    @Query("SELECT cartDetailQuantity FROM CartDetail WHERE cartDetailId = :id and cartDetailBeverage = :beverage")
    public LiveData<Integer> getQuantityOfCartDetail(String id, String beverage);

    @Query("DELETE FROM CartDetail WHERE cartDetailBeverage = :beverage AND cartDetailId = :id")
    public void deleteBeverageInCartDetail(String beverage, String id);



//    ------------------

    @Query("UPDATE CartDetail SET cartDetailQuantity = :quantity WHERE cartDetailId = :id")
    public void updateQuantityBeverage(Integer quantity, String id);

    @Delete
    public void DeleteItemInCartDetail(CartDetail item);

}
