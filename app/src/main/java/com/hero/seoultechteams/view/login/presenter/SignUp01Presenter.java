package com.hero.seoultechteams.view.login.presenter;

import android.text.TextUtils;
import android.util.Patterns;

import com.hero.seoultechteams.view.login.InvalidUserInfoType;
import com.hero.seoultechteams.view.login.contract.SignUp01Contract;



public class SignUp01Presenter implements SignUp01Contract.Presenter {
    private final SignUp01Contract.View view;

    public SignUp01Presenter(SignUp01Contract.View view) {
        this.view = view;
    }

    public boolean checkLoginForm(String email, String pwd, String pwdConfirm) {
        InvalidUserInfoType invalidUserInfoType = null;

        if (TextUtils.isEmpty(email)) {
            invalidUserInfoType = InvalidUserInfoType.EMPTY_EMAIL;
        } else if (TextUtils.isEmpty(pwd)) {
            invalidUserInfoType = InvalidUserInfoType.EMPTY_PWD;
        } else if (!checkEmailValid(email)) {
            invalidUserInfoType = InvalidUserInfoType.INVALID_EMAIL_FORM;
        } else if (!checkPwdValid(pwd)) {
            invalidUserInfoType = InvalidUserInfoType.INVALID_PWD_LENGTH;
        } else if (TextUtils.isEmpty(pwdConfirm) || !pwd.equals(pwdConfirm)) {
            invalidUserInfoType = InvalidUserInfoType.INCORRECT_PASSWORD;
        }

        if(invalidUserInfoType != null) {
            view.onInvalidUserInfo(invalidUserInfoType);
        }

        return invalidUserInfoType == null;
    }

    private boolean checkPwdValid(String pwd) {
        return pwd.length() > 6;
    }

    private boolean checkEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
