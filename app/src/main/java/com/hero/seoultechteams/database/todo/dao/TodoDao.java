package com.hero.seoultechteams.database.todo.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.List;

@Dao
public interface TodoDao extends BaseDao<TodoDao> {
    @Query("SELECT * FROM TodoData")
    List<TodoData> getAllTodos();

    @Query("SELECT * FROM TodoData WHERE userKey = :userKey limit 1")
    TodoData getUserFromKey(String userKey);
}