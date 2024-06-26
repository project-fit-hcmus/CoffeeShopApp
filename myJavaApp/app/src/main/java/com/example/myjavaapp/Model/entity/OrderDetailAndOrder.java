//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import java.util.List;
//
////Ref: "OrderDetail".order_detail_id < "Order".order_id
//// mối quan hệ 1 nhiều
//public class OrderDetailAndOrder {
//    @Embedded public Order order;
//    @Relation(
//            parentColumn = "order_id",
//            entityColumn = "order_detail_id"
//    )
//    public List<OrderDetail> orderDetailList;
//}
