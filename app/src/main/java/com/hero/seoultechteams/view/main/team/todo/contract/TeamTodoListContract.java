package com.hero.seoultechteams.view.main.team.todo.contract;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TeamTodoListContract {
    public interface View {
        void onGetTeamTodoList(List<TodoEntity> data);

        void emptyTeamTodoList();

        void failedGetTeamTodoList();

        void updatedTodoState(TodoEntity data, int position);

        void failedUpdateTodoState();
    }

    public interface Presenter {
        void getTeamTodoDataListFromDatabase(String teamKey);

        void setRefresh(boolean isRefresh);

        void updateTodoState(TodoEntity data, int position);
    }
}