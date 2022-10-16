package com.hero.seoultechteams.view.main.team.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.usecase.GetTeamUseCase;
import com.hero.seoultechteams.domain.team.usecase.UpdateTeamDetailUseCase;
import com.hero.seoultechteams.view.main.team.contract.TeamDetailContract;

public class TeamDetailPresenter implements TeamDetailContract.Presenter {
    private final TeamDetailContract.View view;
    private final UpdateTeamDetailUseCase updateTeamDetailUseCase;
    private final GetTeamUseCase getTeamUseCase;

    private TeamEntity teamEntity;

    public TeamDetailPresenter(TeamDetailContract.View view,
                               UpdateTeamDetailUseCase updateTeamDetailUseCase,
                               GetTeamUseCase getTeamUseCase) {
        this.view = view;
        this.updateTeamDetailUseCase = updateTeamDetailUseCase;
        this.getTeamUseCase = getTeamUseCase;
    }

    @Override
    public void updateTeamDetail(String teamName, String teamDesc) {
        if (teamEntity == null) {
            return;
        }

        teamEntity.setTeamName(teamName);
        teamEntity.setTeamDesc(teamDesc);

        updateTeamDetailUseCase.invoke(new OnCompleteListener<TeamEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TeamEntity data) {
                if (isSuccess) {
                    view.updatedTeamDetail(data);
                } else {
                    view.failedUpdateTeamDetail();
                }
            }
        }, teamEntity);
    }

    @Override
    public void requestTeamData(String teamKey) {
        getTeamUseCase.invoke(new OnCompleteListener<TeamEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TeamEntity data) {
                teamEntity = data;
                if (isSuccess) {
                    view.onLoadTeam(teamEntity);
                } else {
                    view.failedLoadTeam();
                }
            }
        }, teamKey);
    }
}
