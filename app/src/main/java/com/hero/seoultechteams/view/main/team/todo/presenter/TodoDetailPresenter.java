package com.hero.seoultechteams.view.main.team.todo.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.usecase.GetTodoUseCase;
import com.hero.seoultechteams.domain.todo.usecase.UpdateTodoDetailUseCase;
import com.hero.seoultechteams.view.main.team.todo.contract.TodoDetailContract;

public class TodoDetailPresenter implements TodoDetailContract.Presenter {
    private final TodoDetailContract.View view;
    private final UpdateTodoDetailUseCase updateTodoDetailUseCase;
    private final GetTodoUseCase getTodoUseCase;

    private TodoEntity todoEntity;

    public TodoDetailPresenter(TodoDetailContract.View view,
                               UpdateTodoDetailUseCase updateTodoDetailUseCase,
                               GetTodoUseCase getTodoUseCase) {
        this.view = view;
        this.updateTodoDetailUseCase = updateTodoDetailUseCase;
        this.getTodoUseCase = getTodoUseCase;
    }

    @Override
    public void updateTodoDetail(String todoTitle, String todoDesc) {
        if (todoEntity == null) {
            return;
        }

        todoEntity.setTodoTitle(todoTitle);
        todoEntity.setTodoDesc(todoDesc);

        updateTodoDetailUseCase.invoke(new OnCompleteListener<TodoEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TodoEntity data) {
                if (isSuccess) {
                    view.updatedTodoDetail(data);
                } else {
                    view.failedUpdateTodoDetail();
                }
            }
        }, todoEntity);
    }

    @Override
    public void requestTodoData(String todoKey) {
        getTodoUseCase.invoke(new OnCompleteListener<TodoEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TodoEntity data) {
                todoEntity = data;
                if (isSuccess) {
                    view.onLoadTodo(todoEntity);
                } else {
                    view.failedLoadTodo();
                }
            }
        }, todoKey);
    }
}