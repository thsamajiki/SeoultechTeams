package com.hero.seoultechteams;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.team.TeamRepository;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.UserRepository;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.view.login.LoginActivity;
import com.hero.seoultechteams.view.main.MainActivity;

import java.util.ArrayList;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
            getTeamListFromDatabase();
        }
    }

    private void getTeamListFromDatabase() {
        TeamRepository teamRepository = new TeamRepository(this);
        teamRepository.getTeamList(new OnCompleteListener<ArrayList<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TeamData> data) {
                getUserFromDatabase();
            }
        });
    }

    private void getUserFromDatabase() {
        UserRepository userRepository = new UserRepository(this);
        userRepository.getUser(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                if (isSuccess && data != null) {
                    Uri userLocalProfileUri = getCurrentUser().getPhotoUrl();
                    if (userLocalProfileUri != null && !TextUtils.isEmpty(data.getProfileImageUrl())) {
                        String userLocalName = getCurrentUser().getDisplayName();
                        if (!userLocalProfileUri.toString().equals(data.getProfileImageUrl()) ||
                                !userLocalName.equals(data.getName())) {
                            userRepository.updateLocalUser(data);
                        }
                    }
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, getCurrentUser().getUid());
    }
}