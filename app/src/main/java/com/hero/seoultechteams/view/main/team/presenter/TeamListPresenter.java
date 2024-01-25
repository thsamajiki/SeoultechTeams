package com.hero.seoultechteams.view.main.team.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.usecase.GetTeamListUseCase;
import com.hero.seoultechteams.view.main.team.contract.TeamListContract;

import java.util.List;

public class TeamListPresenter implements TeamListContract.Presenter {
    private final TeamListContract.View view;
    private final GetTeamListUseCase getTeamListUseCase;

    public TeamListPresenter(TeamListContract.View view, GetTeamListUseCase getTeamListUseCase) {
        this.view = view;
        this.getTeamListUseCase = getTeamListUseCase;
    }

    @Override
    public void getTeamListFromDatabase() {
        String myUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getTeamListUseCase.invoke(new OnCompleteListener<List<TeamEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamEntity> data) {
                if (isSuccess) {
                    if (data != null) {
                        if (data.isEmpty()) {
                            view.onEmptyTeamList();
                        } else {
                            view.onGetTeamList(data);
                        }
                    }
                } else {
                    view.failedGetTeamList();
                }
            }
        }, myUserKey);
    }
}
