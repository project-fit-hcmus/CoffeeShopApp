//package com.example.myjavaapp.Model.entity;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import java.util.Date;
//
//@Entity(tableName = "Comment")
//public class Comment {
//    @PrimaryKey(autoGenerate = true)
//    private Integer comment_id;
//    @ColumnInfo(name="comment_user")
//    private Integer comment_user;
//    @ColumnInfo(name="comment_order")
//    private Integer comment_order;
//    @ColumnInfo(name="comment_content")
//    private String comment_content;
//    @ColumnInfo(name="comment_rating")
//    private Integer comment_rating;
//    @ColumnInfo(name="comment_timestamp")
//    private Date comment_timestamp;
//    public Integer getCommentId(){return this.comment_id;}
//    public Integer getCommentUser(){return this.comment_user;}
//    public Integer getCommnetOrder(){return  this.comment_order;}
//    public String getCommentContent(){return  this.comment_content;}
//    public Integer getCommentRating(){return  this.comment_rating;}
//    public Date getCommentTimestamp(){return  this.comment_timestamp;}
//    public void setCommentId(Integer id){this.comment_id = id;}
//    public void setCommentUser(Integer user){this.comment_user = user;}
//    public void setCommentOrder(Integer order){this.comment_order = order;}
//    public void setCommentContent(String content){this.comment_content = content;}
//    public void setCommentRating(Integer rating){this.comment_rating = rating;}
//    public void setCommentTimestamp(Date time){this.comment_timestamp = time;}
//}
