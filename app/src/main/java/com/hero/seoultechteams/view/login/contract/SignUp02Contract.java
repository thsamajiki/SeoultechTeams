package com.hero.seoultechteams.view.login.contract;

import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.login.InvalidUserInfoType;

public abstract class SignUp02Contract {
    public interface View {
        void onAddUser(UserEntity userEntity);

        void failedUpdateProfile();

        void failedSignUp();

        void onInvalidUserInfo(InvalidUserInfoType invalidUserInfoType);
    }

    public interface Presenter {
        void signUp(final String email, String pwd, String userName);
    }
}
