package com.hero.seoultechteams.view.main.team.todo.contract;

import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;

import java.util.List;

public abstract class AddTodoContract {
    public interface View {

        void addedTodoList(TodoEntity data);

        void failedAddTodo();

        void onTeamMemberList(List<MemberEntity> data);

        void failedTeamMemberList();
    }
    public interface Presenter {
        void addTodoToDatabase(String addTodoTitle, long todoEndDateTime, MemberEntity managerData, TeamEntity teamEntity);

        void getTeamMemberList(String teamKey);
    }
}
