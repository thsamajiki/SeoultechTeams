package com.hero.seoultechteams.view.main.team.option_menu.contract;

import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;

import java.util.List;

public abstract class TeamMemberListContract {
    public interface View {
        void onGetTeamMemberList(List<MemberEntity> memberData);

        void failedGetTeamMemberList();
    }

    public interface Presenter {
        void getMemberDataListFromDatabase(TeamEntity teamData, List<MemberEntity> teamMemberDataList);
    }
}
