//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
////Ref: "Comment".comment_order < "Order".order_id
//// mối quan hệ 1 : 1  ~ mỗi order chỉ cho phép 1 comment duy nhất
//public class CommentAndOrder {
//    @Embedded public Order order;
//    @Relation(
//            parentColumn = "order_id",
//            entityColumn = "comment_order"
//    )
//    public Comment comment;
//}
