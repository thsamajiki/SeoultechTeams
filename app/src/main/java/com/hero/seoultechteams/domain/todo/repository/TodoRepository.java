package com.hero.seoultechteams.domain.todo.repository;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;

import java.util.List;

public interface TodoRepository {
    void getTodo(final OnCompleteListener<TodoEntity> onCompleteListener, final String todoKey);

    void getTeamTodoList(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String teamKey);

    void getMyTodoList(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String userKey);

    void addTodo(final OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity);

    void updateTodo(final OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity);

    void removeTodo(final OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity);

    void setRefresh(boolean isRefresh);
}
