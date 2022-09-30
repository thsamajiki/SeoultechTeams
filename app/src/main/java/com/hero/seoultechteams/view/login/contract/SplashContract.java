package com.hero.seoultechteams.view.login.contract;

public interface SplashContract {
    interface View {
        void goToMainActivity();

        void failedGetUserInfo();
    }

    interface Presenter {
        void firebaseLogin(String email, String pwd);

        void getTeamListFromDatabase();

        void getUserFromDatabase();
    }
}
