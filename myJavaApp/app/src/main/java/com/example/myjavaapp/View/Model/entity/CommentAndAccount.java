//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import java.util.List;
//
////Ref: "Comment".comment_user < "Account".user_id
//// mối quan hệ 1 nhiều
//public class CommentAndAccount {
//    @Embedded public Account account;
//    @Relation(
//            parentColumn = "user_id",
//            entityColumn =  "comment_user"
//    )
//    public List<Comment> commentList;
//}
