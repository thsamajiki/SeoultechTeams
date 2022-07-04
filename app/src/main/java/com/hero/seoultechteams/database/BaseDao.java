package com.hero.seoultechteams.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import java.util.ArrayList;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BaseDao<T> {
    @Insert(onConflict = REPLACE)
    void insertData(T data);

    @Insert
    void insertAll(ArrayList<T> dataList);

    @Delete
    void delete(T data);


}
