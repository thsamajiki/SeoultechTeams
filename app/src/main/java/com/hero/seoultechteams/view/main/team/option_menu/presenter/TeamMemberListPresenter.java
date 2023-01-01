package com.hero.seoultechteams.view.main.team.option_menu.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.usecase.GetMemberListUseCase;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.main.team.option_menu.contract.TeamMemberListContract;

import java.util.List;

public class TeamMemberListPresenter implements TeamMemberListContract.Presenter {
    private final TeamMemberListContract.View view;
    private final GetMemberListUseCase getMemberListUseCase;

    public TeamMemberListPresenter(TeamMemberListContract.View view, GetMemberListUseCase getMemberListUseCase) {
        this.view = view;
        this.getMemberListUseCase = getMemberListUseCase;
    }

    @Override
    public void getMemberDataListFromDatabase(TeamEntity teamData, List<MemberEntity> teamMemberDataList) {
        String teamKey = teamData.getTeamKey();

        getMemberListUseCase.invoke(new OnCompleteListener<List<MemberEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberEntity> data) {
                if (isSuccess && data != null) {
                    view.onGetTeamMemberList(data);
                } else {
                    view.failedGetTeamMemberList();
                }
            }
        }, teamKey);
    }
}
