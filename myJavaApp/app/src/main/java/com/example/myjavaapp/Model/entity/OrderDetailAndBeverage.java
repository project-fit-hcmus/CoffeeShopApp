package com.example.myjavaapp.Model.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

//Ref: "OrderDetail".order_detail_beverage < "Beverage".beverage_id
// mối quan hệ 1 nhiều
public class OrderDetailAndBeverage {
    @Embedded public OrderDetail orderDetail;
    @Relation(
            parentColumn = "orderDetailBeverage",
            entityColumn = "beverageId"
    )
    public Beverage beverage;
}
