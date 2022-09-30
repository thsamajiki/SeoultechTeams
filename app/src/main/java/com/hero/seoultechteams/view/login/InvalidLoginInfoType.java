package com.hero.seoultechteams.view.login;

import androidx.annotation.StringRes;

import com.hero.seoultechteams.R;

public enum InvalidLoginInfoType {
    INVALID_EMAIL_FORM(R.string.invalid_email_form),
    INCORRECT_PWD(R.string.incorrect_password),
    EMPTY_PWD(R.string.empty_password);

    @StringRes
    private int message;

    InvalidLoginInfoType(Integer message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}