package com.hero.seoultechteams.domain.todo.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.repository.TodoRepository;

public class UpdateTodoStateUseCase {
    private TodoRepository todoRepository;

    public UpdateTodoStateUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void invoke(OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        todoRepository.updateTodo(onCompleteListener, todoEntity);
    }
}
