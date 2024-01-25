package com.hero.seoultechteams.view.main.team.todo.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.usecase.GetTeamTodoListUseCase;
import com.hero.seoultechteams.domain.todo.usecase.SetRefreshUseCase;
import com.hero.seoultechteams.domain.todo.usecase.UpdateTodoStateUseCase;
import com.hero.seoultechteams.view.main.team.todo.contract.TeamTodoListContract;

import java.util.List;

public class TeamTodoListPresenter implements TeamTodoListContract.Presenter {
    private final TeamTodoListContract.View view;
    private final GetTeamTodoListUseCase getTeamTodoListUseCase;
    private final UpdateTodoStateUseCase updateTodoStateUseCase;
    private final SetRefreshUseCase setRefreshUseCase;

    public TeamTodoListPresenter(TeamTodoListContract.View view,
                                 GetTeamTodoListUseCase getTeamTodoListUseCase,
                                 UpdateTodoStateUseCase updateTodoStateUseCase,
                                 SetRefreshUseCase setRefreshUseCase) {
        this.view = view;
        this.getTeamTodoListUseCase = getTeamTodoListUseCase;
        this.updateTodoStateUseCase = updateTodoStateUseCase;
        this.setRefreshUseCase = setRefreshUseCase;
    }

    @Override
    public void getTeamTodoDataListFromDatabase(String teamKey) {
        getTeamTodoListUseCase.invoke(new OnCompleteListener<List<TodoEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoEntity> data) {
                if (isSuccess) {
                    if (data != null) {
                        if (data.isEmpty()) {
                            view.emptyTeamTodoList();
                        } else {
                            view.onGetTeamTodoList(data);
                        }
                    }
                } else {
                    view.failedGetTeamTodoList();
                }
            }
        }, teamKey);
    }

    @Override
    public void setRefresh(boolean isRefresh) {
        setRefreshUseCase.invoke(isRefresh);
    }

    @Override
    public void updateTodoState(TodoEntity data, int position) {
        updateTodoStateUseCase.invoke(new OnCompleteListener<TodoEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TodoEntity data) {
                if(isSuccess) {
                    view.updatedTodoState(data, position);
                } else {
                    view.failedUpdateTodoState();
                }
            }
        }, data);
    }
}
