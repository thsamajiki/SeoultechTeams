package com.hero.seoultechteams.view.main.account.contract;

import com.hero.seoultechteams.domain.user.entity.UserEntity;

public abstract class AccountContract {
    public interface View {
    }

    public interface Presenter {
        UserEntity getUserEntity();
    }
}
