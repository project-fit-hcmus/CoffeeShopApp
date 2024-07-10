package com.example.myjavaapp.Model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "Comment")
public class Comment {
    @PrimaryKey
    @ColumnInfo(name = "commentId")
    @NotNull
    private String commentId;
    @ColumnInfo(name="commentUser")
    @NotNull
    private String commentUser;
    @ColumnInfo(name="commentOrder")
    @NotNull
    private String commentOrder;
    @ColumnInfo(name="commentContent")
    private String commentContent;
    @ColumnInfo(name="commentRating")
    private float commentRating;
    @ColumnInfo(name="commentTimestamp")
    private String commentTimestamp;
    public String getCommentId(){return this.commentId;}
    public String getCommentUser(){return this.commentUser;}
    public String getCommentOrder(){return  this.commentOrder;}
    public String getCommentContent(){return  this.commentContent;}
    public float getCommentRating(){return  this.commentRating;}
    public String getCommentTimestamp(){return  this.commentTimestamp;}
    public void setCommentId(String id){this.commentId = id;}
    public void setCommentUser(String user){this.commentUser = user;}
    public void setCommentOrder(String order){this.commentOrder = order;}
    public void setCommentContent(String content){this.commentContent = content;}
    public void setCommentRating(float rating){this.commentRating = rating;}
    public void setCommentTimestamp(String time){this.commentTimestamp = time;}

    public Comment(){}
    public Comment(String id, String user, String order, String content, float rating, String time){
        this.commentId = id;
        this.commentUser = user;
        this.commentOrder = order;
        this.commentContent = content;
        this.commentRating = rating;
        this.commentTimestamp = time;
    }


}
