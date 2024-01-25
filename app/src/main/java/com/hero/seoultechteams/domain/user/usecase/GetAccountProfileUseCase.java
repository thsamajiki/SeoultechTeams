package com.hero.seoultechteams.domain.user.usecase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

public class GetAccountProfileUseCase {
    private final UserRepository userRepository;

    public GetAccountProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity invoke() {
        return userRepository.getAccountProfile();
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
