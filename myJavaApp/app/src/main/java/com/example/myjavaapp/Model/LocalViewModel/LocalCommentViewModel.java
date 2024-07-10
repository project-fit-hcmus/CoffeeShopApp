package com.example.myjavaapp.Model.LocalViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myjavaapp.Model.entity.Comment;
import com.example.myjavaapp.Model.entity.Type;
import com.example.myjavaapp.Model.repository.CateRepository;
import com.example.myjavaapp.Model.repository.CommentRepository;

import java.util.List;

public class LocalCommentViewModel extends AndroidViewModel {
    private final CommentRepository mRepository;


    public LocalCommentViewModel(Application application){
        super(application);
        this.mRepository = new CommentRepository(application);
    }

    public void insertAll(List<Comment> list){
        mRepository.insertAll(list);
    }
    public void insert(Comment comment){
        mRepository.insert(comment);
    }
    public LiveData<List<Comment>> getAllCommentOfUser(String Uid){
        return mRepository.getAllCommentOfUser(Uid);
    }
    public LiveData<Comment> getCommentOfOrder(String orderId){
        return mRepository.getCommentOfOrder(orderId);
    }
}
