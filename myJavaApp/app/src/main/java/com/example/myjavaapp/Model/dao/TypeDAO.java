package com.example.myjavaapp.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myjavaapp.Model.entity.Type;

import java.util.List;

@Dao
public interface TypeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllType(List<Type> types);
    @Insert
    public void insertType(Type type);
    @Query("SELECT * FROM Type")
    public LiveData<List<Type>> getAllType();
    @Query("SELECT typeName FROM TYPE WHERE typeId = :input ")      // get type name from type id
    public LiveData<String> getNameFromId(String input);
}
