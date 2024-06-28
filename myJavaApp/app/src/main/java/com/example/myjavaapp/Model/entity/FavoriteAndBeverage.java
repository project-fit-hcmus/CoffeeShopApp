package com.example.myjavaapp.Model.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

//Ref: "Favorite".favorite_beverage < "Beverage".beverage_id
// mối quan hệ 1  nhiều
public class FavoriteAndBeverage {

    @Embedded public Favorite favorite;
    @Relation(
            parentColumn = "favoriteBeverage",
            entityColumn = "beverageId"
    )
    public Beverage beverage;
}
