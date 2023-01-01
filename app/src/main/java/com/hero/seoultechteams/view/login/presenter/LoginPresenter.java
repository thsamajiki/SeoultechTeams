package com.hero.seoultechteams.view.login.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.usecase.GetUserUseCase;
import com.hero.seoultechteams.view.login.InvalidLoginInfoType;
import com.hero.seoultechteams.view.login.contract.LoginContract;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;
    private final GetUserUseCase getUserUseCase;
    private static final String TAG = LoginPresenter.class.getName() +" 로그";

    public LoginPresenter(LoginContract.View view, GetUserUseCase getUserUseCase) {
        this.view = view;
        this.getUserUseCase = getUserUseCase;
    }

    @Override
    public void firebaseLogin(String email, String pwd) {
        InvalidLoginInfoType invalidLoginInfoType = null;

        if (!checkEmailValid(email)) {
            invalidLoginInfoType = InvalidLoginInfoType.INVALID_EMAIL_FORM;
        } else if (!checkPwdValid(pwd)) {
            invalidLoginInfoType = InvalidLoginInfoType.INCORRECT_PWD;
        } else if (TextUtils.isEmpty(pwd)) {
            invalidLoginInfoType = InvalidLoginInfoType.EMPTY_PWD;
        }

        if (invalidLoginInfoType != null) {
            view.onInvalidLogin(invalidLoginInfoType);
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.i(TAG, "firebaseLogin: getUser = "+ mAuth.getCurrentUser());

//        getUserUseCase.invoke(new OnCompleteListener<UserEntity>() {
//            @Override
//            public void onComplete(boolean isSuccess, UserEntity data) {
//
//            }
//        }, getCurrentUser().getUid());

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        getUserFromDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.failedLogin();
                    }
                });
    }

    private boolean checkPwdValid(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }

        return pwd.length() >= 6;
    }

    private boolean checkEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void getUserFromDatabase() {
        getUserUseCase.invoke(new OnCompleteListener<UserEntity>() {
            @Override
            public void onComplete(boolean isSuccess, UserEntity data) {
                if (isSuccess && data != null) {
                    view.onLogin(data);
                }
            }
        }, getCurrentUser().getUid());
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}