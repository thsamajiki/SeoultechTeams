package com.hero.seoultechteams.domain.todo.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class GetMyTodoListUseCase {

    private TodoRepository todoRepository;

    public GetMyTodoListUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void invoke(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String userKey) {
        todoRepository.getMyTodoList(onCompleteListener, userKey);
    }
}