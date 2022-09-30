package com.hero.seoultechteams.view.main;

import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.utils.CacheManager;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private SignOutUseCase signOutUseCase;

    public MainPresenter(MainContract.View view, SignOutUseCase signOutUseCase) {
        this.view = view;
        this.signOutUseCase = signOutUseCase;
    }

    @Override
    public void signOut() {
        signOutUseCase.invoke();
    }
}
