package com.hero.seoultechteams.view.main.mytodo.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.usecase.GetTeamListUseCase;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.usecase.GetMyTodoListUseCase;
import com.hero.seoultechteams.domain.todo.usecase.SetRefreshUseCase;
import com.hero.seoultechteams.domain.todo.usecase.UpdateTodoStateUseCase;
import com.hero.seoultechteams.view.main.mytodo.contract.MyTodoListContract;

import java.util.ArrayList;
import java.util.List;

public class MyTodoListPresenter implements MyTodoListContract.Presenter {
    private final MyTodoListContract.View view;
    private final GetMyTodoListUseCase getMyTodoListUseCase;
    private final GetTeamListUseCase getTeamListUseCase;
    private final SetRefreshUseCase setRefreshUseCase;
    private final UpdateTodoStateUseCase updateTodoStateUseCase;

    public MyTodoListPresenter(MyTodoListContract.View view,
                               GetMyTodoListUseCase getMyTodoListUseCase,
                               GetTeamListUseCase getTeamListUseCase,
                               SetRefreshUseCase setRefreshUseCase,
                               UpdateTodoStateUseCase updateTodoStateUseCase) {
        this.view = view;
        this.getMyTodoListUseCase = getMyTodoListUseCase;
        this.getTeamListUseCase = getTeamListUseCase;
        this.setRefreshUseCase = setRefreshUseCase;
        this.updateTodoStateUseCase = updateTodoStateUseCase;
    }

    @Override
    public void getMyTodoListFromDatabase() {
        String myUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getMyTodoListUseCase.invoke(new OnCompleteListener<List<TodoEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoEntity> data) {
                if (isSuccess) {
                    if (data != null) {
                        view.onGetMyTodoList(data);
                    } else {
                        view.emptyMyTodoList();
                    }
                } else {
                    view.failedGetMyTodoList();
                }
            }
        }, myUserKey);
    }

    @Override
    public void getTeamList() {
        String myUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getTeamListUseCase.invoke(new OnCompleteListener<List<TeamEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamEntity> data) {
                view.setMyTeamDataList(data);
            }
        }, myUserKey);
    }

    @Override
    public void setRefresh(boolean isRefresh) {
        setRefreshUseCase.invoke(isRefresh);
    }

    @Override
    public void updateTodo(TodoEntity todoEntity, int position) {
        updateTodoStateUseCase.invoke(new OnCompleteListener<TodoEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TodoEntity data) {
                if(isSuccess) {
                    view.updatedTodoState(data, position);
                } else {
                    view.failedUpdateTodo();
                }
            }
        }, todoEntity);
    }
}
