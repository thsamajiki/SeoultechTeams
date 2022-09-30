package com.hero.seoultechteams.view.login;

import androidx.annotation.StringRes;

import com.hero.seoultechteams.R;

public enum InvalidUserInfoType {
    EMPTY_EMAIL(R.string.empty_email),
    EMPTY_PWD(R.string.empty_password),
    EMPTY_NAME(R.string.empty_user_name),
    INVALID_EMAIL_FORM(R.string.invalid_email_form),
    INVALID_PWD_LENGTH(R.string.invalid_password_length),
    INCORRECT_PASSWORD(R.string.incorrect_password),;
    
    @StringRes
    private int message;
    
    InvalidUserInfoType(Integer message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}