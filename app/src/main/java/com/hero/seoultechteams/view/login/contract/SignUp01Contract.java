package com.hero.seoultechteams.view.login.contract;

import com.hero.seoultechteams.view.login.InvalidUserInfoType;

public interface SignUp01Contract {

    interface View {
        void onInvalidUserInfo(InvalidUserInfoType invalidUserInfoType);
    }

    interface Presenter {
        boolean checkLoginForm(String email, String pwd, String pwdConfirm);
    }
}
