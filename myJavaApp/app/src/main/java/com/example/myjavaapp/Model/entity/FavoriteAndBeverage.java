//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
////Ref: "Favorite".favorite_beverage < "Beverage".beverage_id
//// mối quan hệ 1  nhiều
//public class FavoriteAndBeverage {
//
//    @Embedded public Beverage beverage;
//    @Relation(
//            parentColumn = "beverage_id",
//            entityColumn = "favorite_beverage"
//    )
//    public Favorite favorite;
//}
