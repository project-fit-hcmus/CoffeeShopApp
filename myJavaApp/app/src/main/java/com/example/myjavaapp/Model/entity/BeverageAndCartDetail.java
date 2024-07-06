package com.example.myjavaapp.Model.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

//public class BeverageAndCartDetail {
//    @Embedded public CartDetail cartDetail;
//    @Relation(
//            parentColumn = "cartDetailBeverage",
//            entityColumn = "beverageId"
//    )
//    public Beverage beverage;
//}

public class BeverageAndCartDetail {
   @Embedded public Beverage beverage;
   @Relation(
           parentColumn = "beverageId",
           entityColumn = "cartDetailBeverage"
   )
    public CartDetail cartDetail;
   @Relation(
           parentColumn = "beverageType",
           entityColumn = "TypeId"
   )
    public Type type;
}