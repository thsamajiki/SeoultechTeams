package com.hero.seoultechteams.database.todo.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.List;

@Dao
public interface TodoDao extends BaseDao<TodoData> {
    @Query("SELECT * FROM todo_table")
    List<TodoData> getAllTodos();

    @Query("SELECT * FROM todo_table WHERE todoKey = :todoKey limit 1")
    TodoData getTodoFromKey(String todoKey);

    @Query("SELECT * FROM todo_table WHERE userKey = :userKey limit 1")
    TodoData getManagerFromKey(String userKey);
}
