package com.hero.seoultechteams.view.main.team.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.usecase.AddTeamUseCase;
import com.hero.seoultechteams.domain.user.usecase.GetAccountProfileUseCase;
import com.hero.seoultechteams.view.main.team.contract.CreateTeamContract;

public class CreateTeamPresenter implements CreateTeamContract.Presenter {
    private final CreateTeamContract.View view;
    private final AddTeamUseCase addTeamUseCase;
    private final GetAccountProfileUseCase getAccountProfileUseCase;

    public CreateTeamPresenter(CreateTeamContract.View view,
                               AddTeamUseCase addTeamUseCase,
                               GetAccountProfileUseCase getAccountProfileUseCase) {
        this.view = view;
        this.addTeamUseCase = addTeamUseCase;
        this.getAccountProfileUseCase = getAccountProfileUseCase;
    }

    @Override
    public void addTeamToDatabase(String teamName, String teamDesc) {
        String userKey = getAccountProfileUseCase.invoke().getKey();
        TeamEntity team = createTeamData(teamName, teamDesc, userKey);

        addTeamUseCase.invoke(new OnCompleteListener<TeamEntity>() {
            @Override
            public void onComplete(boolean isSuccess, TeamEntity data) {
                if (isSuccess) {
                    view.addedTeamList(data);
                } else {
                    view.failedAddTeam();
                }
            }
        }, team);
    }

    private TeamEntity createTeamData(String teamName, String teamDesc, String userKey) {
        return new TeamEntity(
                teamName,
                teamDesc,
                userKey,
                System.currentTimeMillis(),
                null
        );
    }
}
