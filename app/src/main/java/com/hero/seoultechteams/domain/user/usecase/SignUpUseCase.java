package com.hero.seoultechteams.domain.user.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

public class SignUpUseCase {
    private final UserRepository userRepository;

    public SignUpUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<UserEntity> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd) {
        userRepository.addUser(onCompleteListener, onFailedListener, userName, email, pwd);
    }
}