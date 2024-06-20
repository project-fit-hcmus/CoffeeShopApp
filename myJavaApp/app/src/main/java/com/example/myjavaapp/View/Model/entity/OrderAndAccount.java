//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import java.util.List;
//
////Ref: "Order".order_user < "Account".user_id
//// mối quan hệ 1 nhiều
//public class OrderAndAccount {
//    @Embedded public Account account;
//    @Relation(
//            parentColumn = "user_id",
//            entityColumn = "order_user"
//    )
//    public List<Order> orderList;
//}
