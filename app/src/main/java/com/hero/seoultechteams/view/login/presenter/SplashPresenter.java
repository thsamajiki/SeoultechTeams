package com.hero.seoultechteams.view.login.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.usecase.GetTeamListUseCase;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.usecase.GetAccountProfileUseCase;
import com.hero.seoultechteams.domain.user.usecase.GetUserUseCase;
import com.hero.seoultechteams.view.login.contract.SplashContract;

import java.util.List;

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;
    private GetTeamListUseCase getTeamListUseCase;
    private GetUserUseCase getUserUseCase;
    private GetAccountProfileUseCase getAccountProfileUseCase;

    public SplashPresenter(SplashContract.View view, GetTeamListUseCase getTeamListUseCase, GetUserUseCase getUserUseCase, GetAccountProfileUseCase getAccountProfileUseCase) {
        this.view = view;
        this.getTeamListUseCase = getTeamListUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getAccountProfileUseCase = getAccountProfileUseCase;
    }

    @Override
    public void firebaseLogin(String email, String pwd) {

    }

    @Override
    public void getTeamListFromDatabase() {
        getTeamListUseCase.invoke(new OnCompleteListener<List<TeamEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamEntity> data) {
                getUserFromDatabase();
            }
        }, getCurrentUser().getUid());
    }

    @Override
    public void getUserFromDatabase() {
        getUserUseCase.invoke(new OnCompleteListener<UserEntity>() {
            @Override
            public void onComplete(boolean isSuccess, UserEntity data) {
                if (isSuccess && data != null) {
                    view.goToMainActivity();
                } else {
                    view.failedGetUserInfo();
                }
            }
        }, getCurrentUser().getUid());
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
