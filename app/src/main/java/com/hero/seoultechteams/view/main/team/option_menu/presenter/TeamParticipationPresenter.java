package com.hero.seoultechteams.view.main.team.option_menu.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;

import com.hero.seoultechteams.domain.team.entity.MemberParticipationList;
import com.hero.seoultechteams.domain.team.usecase.GetMemberParticipationUseCase;
import com.hero.seoultechteams.view.main.team.option_menu.contract.TeamParticipationContract;

public class TeamParticipationPresenter implements TeamParticipationContract.Presenter {
    private final TeamParticipationContract.View view;
    private final GetMemberParticipationUseCase getMemberParticipationUseCase;

    public TeamParticipationPresenter(TeamParticipationContract.View view,
                                      GetMemberParticipationUseCase getMemberParticipationUseCase) {
        this.view = view;
        this.getMemberParticipationUseCase = getMemberParticipationUseCase;
    }

    @Override
    public void getMemberParticipationList(String teamKey) {
        getMemberParticipationUseCase.invoke(new OnCompleteListener<MemberParticipationList>() {
            @Override
            public void onComplete(boolean isSuccess, MemberParticipationList memberParticipationList) {
                if (isSuccess) {
                    view.showTeamParticipationRanking(memberParticipationList.getMemberFaithfulRanking());
                    view.showMemberPerformanceKing(memberParticipationList.getMemberPerformanceKing());
                    view.showMemberLatestKing(memberParticipationList.getMemberLatestKing());
                } else {
                    view.failedShowTeamParticipationRanking();
                }
            }
        }, teamKey);
    }
}
