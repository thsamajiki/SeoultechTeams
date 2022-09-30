package com.hero.seoultechteams.view.main.account.setting.contract;

public abstract class SettingContract {
    public interface View {
        void onGetSetting();

        void failedGetSetting();
    }

    public interface Presenter {
        void getSettingFromDatabase();
    }
}