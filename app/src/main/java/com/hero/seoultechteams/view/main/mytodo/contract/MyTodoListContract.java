package com.hero.seoultechteams.view.main.mytodo.contract;

import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class MyTodoListContract {
    public interface View {
        void onGetMyTodoList(List<TodoEntity> data);

        void emptyMyTodoList();

        void failedGetMyTodoList();

        void setMyTeamDataList(List<TeamEntity> data);

        void updatedTodoState(TodoEntity data, int position);

        void failedUpdateTodo();
    }

    public interface Presenter {
        void getMyTodoListFromDatabase();

        void getTeamList();

        void setRefresh(boolean isRefresh);

        void updateTodo(TodoEntity todoEntity, int position);
    }
}