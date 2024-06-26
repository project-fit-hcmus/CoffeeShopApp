package com.example.myjavaapp.Model.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

//Ref: "CartDetail".cart_detail_id < "Cart".cart_id
// mối quan hệ 1 nhiều
public class CartDetailAndCart {
    @Embedded public Cart cart;
    @Relation(
            parentColumn = "cart_id",
            entityColumn = "cart_detail_id"
    )
    public List<CartDetail> cartDetail;

}
