package com.hero.seoultechteams.view.main;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private final SignOutUseCase signOutUseCase;

    public MainPresenter(MainContract.View view, SignOutUseCase signOutUseCase) {
        this.view = view;
        this.signOutUseCase = signOutUseCase;
    }

    @Override
    public void signOut() {
        signOutUseCase.invoke();
    }
}
