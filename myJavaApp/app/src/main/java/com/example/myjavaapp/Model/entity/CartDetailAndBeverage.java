//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import java.util.List;
//
////Ref: "CartDetail".cart_detail_beverage < "Beverage".beverage_id
//// mối quan hệ 1 nhiều
//public class CartDetailAndBeverage {
//    @Embedded public Beverage beverage;
//    @Relation(
//            parentColumn =  "beverage_id",
//            entityColumn =  "cart_detail_beverage"
//    )
//    public List<CartDetail> cartDetailList;
//}
