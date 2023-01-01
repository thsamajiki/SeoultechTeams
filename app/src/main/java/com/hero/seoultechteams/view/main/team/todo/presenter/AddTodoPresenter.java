package com.hero.seoultechteams.view.main.team.todo.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.usecase.GetMemberListUseCase;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.usecase.AddTodoUseCase;
import com.hero.seoultechteams.view.main.team.todo.contract.AddTodoContract;

import java.util.ArrayList;
import java.util.List;

public class AddTodoPresenter implements AddTodoContract.Presenter {
    private final AddTodoContract.View view;
    private final AddTodoUseCase addTodoUseCase;
    private final GetMemberListUseCase getMemberListUseCase;

    public AddTodoPresenter(AddTodoContract.View view, AddTodoUseCase addTodoUseCase, GetMemberListUseCase getMemberListUseCase) {
        this.view = view;
        this.addTodoUseCase = addTodoUseCase;
        this.getMemberListUseCase = getMemberListUseCase;
    }

    @Override
    public void addTodoToDatabase(String addTodoTitle, long todoEndDateTime, MemberEntity managerData, TeamEntity teamEntity) {

        TodoEntity todo = createTodoData(addTodoTitle, todoEndDateTime, managerData, teamEntity);

        addTodoUseCase.invoke(new OnCompleteListener<TodoEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TodoEntity data) {
                if (isSuccess) {
                    view.addedTodoList(data);
                } else {
                    view.failedAddTodo();
                }
            }
        }, todo);
    }

    @Override
    public void getTeamMemberList(String teamKey) {
        getMemberListUseCase.invoke(new OnCompleteListener<List<MemberEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberEntity> data) {
                if (isSuccess && data != null) {
                    view.onTeamMemberList(data);
                } else {
                    view.failedTeamMemberList();
                }
            }
        }, teamKey);
    }


    private TodoEntity createTodoData(String addTodoTitle, long todoEndDateTime, MemberEntity managerData, TeamEntity teamData) {
        ArrayList<Event> eventHistory = new ArrayList<>();
        Event event = new Event();
        event.setEvent(Event.EVENT_CREATE);
        event.setTime(System.currentTimeMillis());
        eventHistory.add(event);

        return new TodoEntity(
                addTodoTitle,
                null,
                managerData.getKey(),
                managerData.getProfileImageUrl(),
                managerData.getName(),
                managerData.getEmail(),
                TodoEntity.TODO_STATE_IN_PROGRESS,
                teamData.getTeamName(),
                teamData.getTeamKey(),
                System.currentTimeMillis(),
                todoEndDateTime,
                eventHistory,
                null
        );
    }
}