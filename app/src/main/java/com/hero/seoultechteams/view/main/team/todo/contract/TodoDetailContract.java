package com.hero.seoultechteams.view.main.team.todo.contract;

import com.hero.seoultechteams.domain.todo.entity.TodoEntity;

public abstract class TodoDetailContract {
    public interface View {
        void updatedTodoDetail(TodoEntity data);

        void failedUpdateTodoDetail();

        void onLoadTodo(TodoEntity data);

        void failedLoadTodo();
    }

    public interface Presenter {
        void updateTodoDetail(String todoTitle, String todoDesc);

        void requestTodoData(String todoKey);
    }
}