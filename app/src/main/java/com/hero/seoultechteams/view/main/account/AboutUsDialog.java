package com.hero.seoultechteams.view.main.account;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.R;


public class AboutUsDialog {

    private final Context context;

    public AboutUsDialog(Context context) {
        this.context = context;
    }

    public void getAboutUsDialog() {
//        String version = BuildConfig.VERSION_NAME;

        new MaterialAlertDialogBuilder(context)
                .setTitle("앱의 정보")
                .setView(R.layout.dialog_about_us)
                .setPositiveButton("닫기", null)
                .create()
                .show();
    }
}