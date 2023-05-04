package com.hero.seoultechteams.view.main.account.setting.presenter;

import android.util.Log;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.usecase.GetAccountProfileUseCase;
import com.hero.seoultechteams.domain.user.usecase.RemoveUserUseCase;
import com.hero.seoultechteams.view.main.account.setting.contract.SettingContract;

public class SettingPresenter implements SettingContract.Presenter {
    private final SettingContract.View view;
//    private final GetSettingUseCase getSettingUseCase;
    private GetAccountProfileUseCase getAccountProfileUseCase;
    private final RemoveUserUseCase removeUserUseCase;

    public SettingPresenter(SettingContract.View view, GetAccountProfileUseCase getAccountProfileUseCase, RemoveUserUseCase removeUserUseCase) {
        this.view = view;
        this.getAccountProfileUseCase = getAccountProfileUseCase;
        this.removeUserUseCase = removeUserUseCase;
    }

    public void getSettingFromDatabase() {

    }

    @Override
    public UserEntity getUserEntity() {
        return getAccountProfileUseCase.invoke();
    }

    @Override
    public void dropOut(UserEntity userEntity) {
        removeUserUseCase.invoke(new OnCompleteListener<UserEntity>() {
            @Override
            public void onComplete(boolean isSuccess, UserEntity data) {
                if (isSuccess) {
                    view.onDropOut();
                    Log.d("SettingPresenter", "onComplete: remove success");
                } else {
                    view.failedDropOut();
                    Log.d("SettingPresenter", "onComplete: remove failed");
                }
            }
        }, new OnFailedListener() {
            @Override
            public void onFailed(Exception exception) {
                exception.getCause();
                view.failedDropOut();
                Log.d("SettingPresenter", "OnFailedListener: remove failed");
            }
        }, userEntity);
    }
}
