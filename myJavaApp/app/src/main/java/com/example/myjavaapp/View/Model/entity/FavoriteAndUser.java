//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import com.example.myjavaapp.Model.User;
//
//import java.util.List;
//
////Ref: "Favorite".favorite_user < "Account".user_id
//// mối quan hệ 1 nhiều
//public class FavoriteAndUser {
//    @Embedded public User user;
//    @Relation(
//            parentColumn = "user_id",
//            entityColumn = "favorite_user"
//    )
//    public List<Favorite> favoriteList;
//}
