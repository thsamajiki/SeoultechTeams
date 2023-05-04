package com.hero.seoultechteams.view.main.account.setting.contract;

import com.hero.seoultechteams.domain.user.entity.UserEntity;

public abstract class SettingContract {
    public interface View {
        void onGetSetting();

        void failedGetSetting();

        void onDropOut();

        void failedDropOut();
    }

    public interface Presenter {
        void getSettingFromDatabase();

        UserEntity getUserEntity();

        void dropOut(UserEntity userEntity);
    }
}