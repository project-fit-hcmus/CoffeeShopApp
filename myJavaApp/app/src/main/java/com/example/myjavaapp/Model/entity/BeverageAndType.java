package com.example.myjavaapp.Model.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.myjavaapp.Model.dao.TypeDAO;

import java.util.List;

//Ref: "Beverage".beverage_type < "TypeOfBeverage".type_id
// mối quan hệ 1 và 1 (1 beverage tương ứng với duy nhất 1 type)
public class BeverageAndType {
    @Embedded public Beverage beverage;
    @Relation(
            parentColumn = "beverageType",
            entityColumn = "TypeId"
    )
    public Type type;
}
