package com.hero.seoultechteams.view.login.contract;


import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.login.InvalidLoginInfoType;

public abstract class LoginContract {
    public interface View {
        void onLogin(UserEntity userEntity);

        void failedLogin();

        void onInvalidLogin(InvalidLoginInfoType invalidLoginInfoType);
    }

    public interface Presenter {
        void firebaseLogin(String email, String pwd);
    }
}
