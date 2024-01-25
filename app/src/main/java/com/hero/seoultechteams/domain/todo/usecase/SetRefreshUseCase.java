package com.hero.seoultechteams.domain.todo.usecase;

import com.hero.seoultechteams.domain.todo.repository.TodoRepository;

public class SetRefreshUseCase {
    private final TodoRepository todoRepository;

    public SetRefreshUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void invoke(boolean isRefresh) {
        todoRepository.setRefresh(isRefresh);
    }
}
