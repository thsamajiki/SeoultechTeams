package com.hero.seoultechteams.view.main;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.R;


public class OpenSourceLicenseDialog {
    private Context context;

    public OpenSourceLicenseDialog(Context context) {
        this.context = context;
    }

    public void getOpenSourceLicenseDialog() {

        new MaterialAlertDialogBuilder(context)
                .setTitle("오픈소스 라이센스")
                .setView(R.layout.dialog_open_source_license)
                .setPositiveButton("닫기", null)
                .create()
                .show();
    }
}
