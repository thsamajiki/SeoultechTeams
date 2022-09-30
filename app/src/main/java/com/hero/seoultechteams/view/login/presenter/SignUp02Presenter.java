package com.hero.seoultechteams.view.login.presenter;

import android.text.TextUtils;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.exception.FailedCreateUserException;
import com.hero.seoultechteams.domain.exception.FailedUpdateUserException;
import com.hero.seoultechteams.domain.exception.UserEmptyException;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.usecase.SignUpUseCase;
import com.hero.seoultechteams.view.login.InvalidUserInfoType;
import com.hero.seoultechteams.view.login.contract.SignUp02Contract;

public class SignUp02Presenter implements SignUp02Contract.Presenter {
    private SignUp02Contract.View view;
    private SignUpUseCase signUpUseCase;

    public SignUp02Presenter(SignUp02Contract.View view, SignUpUseCase signUpUseCase) {
        this.view = view;
        this.signUpUseCase = signUpUseCase;
    }

    @Override
    public void signUp(String email, String pwd, String userName) {
        if (TextUtils.isEmpty(userName)) {
            view.onInvalidUserInfo(InvalidUserInfoType.EMPTY_NAME);
            return;
        }

        signUpUseCase.invoke(new OnCompleteListener<UserEntity>() {
            @Override
            public void onComplete(boolean isSuccess, UserEntity data) {
                if (isSuccess && data != null) {
                    view.onAddUser(data);
                } else {
                    view.failedUpdateProfile();
                }
            }
        }, new OnFailedListener() {
            @Override
            public void onFailed(Exception exception) {
                exception.getCause();

                if (exception instanceof UserEmptyException) {
                    view.failedSignUp();
                } else if (exception instanceof FailedCreateUserException) {
                    view.failedSignUp();
                } else if (exception instanceof FailedUpdateUserException) {
                    view.failedUpdateProfile();

                }
            }
        }, userName, email, pwd);
    }
}
