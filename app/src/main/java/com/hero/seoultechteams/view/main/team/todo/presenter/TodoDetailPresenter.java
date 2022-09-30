package com.hero.seoultechteams.view.main.team.todo.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.usecase.UpdateTodoDetailUseCase;
import com.hero.seoultechteams.view.main.team.todo.contract.TodoDetailContract;

public class TodoDetailPresenter implements TodoDetailContract.Presenter {
    private TodoDetailContract.View view;
    private UpdateTodoDetailUseCase updateTodoDetailUseCase;

    public TodoDetailPresenter(TodoDetailContract.View view, UpdateTodoDetailUseCase updateTodoDetailUseCase) {
        this.view = view;
        this.updateTodoDetailUseCase = updateTodoDetailUseCase;
    }

    @Override
    public void updateTodoDetail(String todoTitle, String todoDesc, TodoEntity data) {
        data.setTodoTitle(todoTitle);
        data.setTodoDesc(todoDesc);

        updateTodoDetailUseCase.invoke(new OnCompleteListener<TodoEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TodoEntity data) {
                if (isSuccess) {
                    view.updatedTodoDetail(data);
                } else {
                    view.failedUpdateTodoDetail();
                }
            }
        }, data);
    }
}