package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.Comment;

import java.util.List;

@Dao
public interface CommentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Comment> lists);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Comment comment);

    @Query("SELECT * FROM Comment WHERE commentUser = :Uid")
    public LiveData<List<Comment>> getAllComment(String Uid);
    @Query("SELECT * FROM Comment WHERE commentOrder = :orderId")
    public LiveData<Comment> getCommentOfOrder(String orderId);

}
