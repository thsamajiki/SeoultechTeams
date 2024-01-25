package com.hero.seoultechteams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.databinding.ActivitySplashBinding;
import com.hero.seoultechteams.view.login.LoginActivity;
import com.hero.seoultechteams.view.login.contract.SplashContract;
import com.hero.seoultechteams.view.login.presenter.SplashPresenter;
import com.hero.seoultechteams.view.main.MainActivity;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> implements SplashContract.View {

    private final SplashContract.Presenter presenter = new SplashPresenter(this,
            Injector.getInstance().provideGetTeamListUseCase(),
            Injector.getInstance().provideGetUserUseCase(),
            Injector.getInstance().provideGetAccountProfileUseCase());

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLogin();
            }
        }, 1000);
    }

    private void autoLogin() {
        if (getCurrentUser() == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else {
            presenter.getTeamListFromDatabase();
        }
    }

    @Override
    public void goToMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void failedGetUserInfo() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.splash_error_title)
                .setMessage(R.string.splash_error_message)
                .setPositiveButton(R.string.splash_error_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .create()
                .show();
    }
}
