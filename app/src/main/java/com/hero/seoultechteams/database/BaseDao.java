package com.hero.seoultechteams.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


public interface BaseDao<T> {
    @Insert(onConflict = REPLACE)
    void insertData(T data);

    @Insert
    void insertAll(List<T> dataList);

    @Update
    void updateData(T data);

    @Delete
    void deleteData(T data);
}