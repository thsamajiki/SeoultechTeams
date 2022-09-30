package com.hero.seoultechteams.view.main;

import com.hero.seoultechteams.domain.user.repository.UserRepository;

public class SignOutUseCase {
    private UserRepository userRepository;

    public SignOutUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke() {
        userRepository.signOut();
    }
}