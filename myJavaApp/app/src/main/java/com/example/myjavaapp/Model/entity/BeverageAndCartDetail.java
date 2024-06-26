package com.example.myjavaapp.Model.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

public class BeverageAndCartDetail {
    @Embedded public CartDetail cartDetail;
    @Relation(
            parentColumn = "cartDetailBeverage",
            entityColumn = "beverageId"
    )
    public Beverage beverage;
}
