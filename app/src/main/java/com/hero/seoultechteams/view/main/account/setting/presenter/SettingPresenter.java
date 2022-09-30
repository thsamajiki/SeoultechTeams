package com.hero.seoultechteams.view.main.account.setting.presenter;

import android.content.Context;

import com.hero.seoultechteams.domain.setting.usecase.GetSettingUseCase;
import com.hero.seoultechteams.view.main.account.setting.contract.SettingContract;

public class SettingPresenter implements SettingContract.Presenter {
    private final SettingContract.View view;
    private final GetSettingUseCase getSettingUseCase;

    public SettingPresenter(SettingContract.View view, GetSettingUseCase getSettingUseCase) {
        this.view = view;
        this.getSettingUseCase = getSettingUseCase;
    }

    public void getSettingFromDatabase() {

    }
}
