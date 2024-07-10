package com.example.myjavaapp.Model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.dao.CommentDAO;
import com.example.myjavaapp.Model.dao.FavoriteDAO;
import com.example.myjavaapp.Model.database.AppDatabase;
import com.example.myjavaapp.Model.entity.Comment;
import com.example.myjavaapp.Model.entity.Favorite;

import java.util.List;

public class CommentRepository {
    private final CommentDAO commentDAO;
    private Application application;
    public CommentRepository(Application application){
        this.application = application;
        commentDAO = AppDatabase.getDatabase(application).commentDAO();
    }
    public void insertAll(List<Comment> list){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            commentDAO.insertAll(list);
        });
    }
    public void insert(Comment comment){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            commentDAO.insert(comment);
        });
    }

    public LiveData<List<Comment>> getAllCommentOfUser(String Uid){
        return commentDAO.getAllComment(Uid);
    }
    public LiveData<Comment> getCommentOfOrder(String orderId){
        return commentDAO.getCommentOfOrder(orderId);
    }
}
