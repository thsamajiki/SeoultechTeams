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

import java.util.ArrayList;

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
        getTeamListUseCase.invoke(new OnCompleteListener<ArrayList<TeamEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TeamEntity> data) {
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
                    // 1. updateLocalUser 동작은 흐름이 -> data

                    // 여기는 사용자 정보를 가져오는 데 성공했으므로
                    // 다른 화면으로 가거나 이런 것들을 함.

                    // 2. MainActivity presenter -> view로 main으로 가라.
                    view.goToMainActivity();
                } else {
                    view.failedGetUserInfo();
                }
            }
        }, getCurrentUser().getUid());
//        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(this);
//        userRepositoryImpl.getUser(new OnCompleteListener<UserData>() {
//            @Override
//            public void onComplete(boolean isSuccess, UserData data) {
//                if (isSuccess && data != null) {
//                    Uri userLocalProfileUri = getCurrentUser().getPhotoUrl();
//                    if (userLocalProfileUri != null && !TextUtils.isEmpty(data.getProfileImageUrl())) {
//                        String userLocalName = getCurrentUser().getDisplayName();
//                        if (!userLocalProfileUri.toString().equals(data.getProfileImageUrl()) ||
//                                !userLocalName.equals(data.getName())) {
//                            userRepositoryImpl.updateLocalUser(data);
//                        }
//                    }
//                }
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//        }, getCurrentUser().getUid());
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
