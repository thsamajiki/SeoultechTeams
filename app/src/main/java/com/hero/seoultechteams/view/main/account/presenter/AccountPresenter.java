package com.hero.seoultechteams.view.main.account.presenter;

import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.usecase.GetAccountProfileUseCase;
import com.hero.seoultechteams.view.main.account.contract.AccountContract;

public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.View view;
    private GetAccountProfileUseCase getAccountProfileUseCase;

    public AccountPresenter(AccountContract.View view, GetAccountProfileUseCase getAccountProfileUseCase) {
        this.view = view;
        this.getAccountProfileUseCase = getAccountProfileUseCase;
    }

    @Override
    public UserEntity getUserEntity() {
        return getAccountProfileUseCase.invoke();
    }
}