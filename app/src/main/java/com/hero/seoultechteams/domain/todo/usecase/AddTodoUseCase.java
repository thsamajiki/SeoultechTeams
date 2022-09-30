package com.hero.seoultechteams.domain.todo.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.repository.TodoRepository;

public class AddTodoUseCase {
    private final TodoRepository todoRepository;

    public AddTodoUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void invoke(final OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        todoRepository.addTodo(onCompleteListener, todoEntity);
    }
}
