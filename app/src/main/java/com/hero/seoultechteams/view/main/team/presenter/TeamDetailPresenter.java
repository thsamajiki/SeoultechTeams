package com.hero.seoultechteams.view.main.team.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.usecase.UpdateTeamDetailUseCase;
import com.hero.seoultechteams.view.main.team.contract.TeamDetailContract;

public class TeamDetailPresenter implements TeamDetailContract.Presenter {
    private TeamDetailContract.View view;
    private UpdateTeamDetailUseCase updateTeamDetailUseCase;

    public TeamDetailPresenter(TeamDetailContract.View view, UpdateTeamDetailUseCase updateTeamDetailUseCase) {
        this.view = view;
        this.updateTeamDetailUseCase = updateTeamDetailUseCase;
    }

    @Override
    public void updateTeamDetail(String teamName, String teamDesc, TeamEntity data) {
        data.setTeamName(teamName);
        data.setTeamDesc(teamDesc);

        updateTeamDetailUseCase.invoke(new OnCompleteListener<TeamEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TeamEntity data) {
                if (isSuccess) {
                    view.updatedTeamDetail(data);
                } else {
                    view.failedUpdateTeamDetail();
                }
            }
        }, data);
    }
}
