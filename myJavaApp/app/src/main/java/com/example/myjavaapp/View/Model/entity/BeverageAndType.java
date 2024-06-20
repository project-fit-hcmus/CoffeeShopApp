//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import com.example.myjavaapp.Model.dao.TypeDAO;
//
//import java.util.List;
//
////Ref: "Beverage".beverage_type < "TypeOfBeverage".type_id
//// mối quan hệ 1 nhiều
//public class BeverageAndType {
//    @Embedded public Type type;
//    @Relation(
//            parentColumn = "type_id",
//            entityColumn = "beverage_type"
//    )
//    public List<Beverage> beverageList;
//}
