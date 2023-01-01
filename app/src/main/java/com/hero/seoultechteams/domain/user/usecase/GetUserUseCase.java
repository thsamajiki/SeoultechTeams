package com.hero.seoultechteams.domain.user.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

public class GetUserUseCase {
    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<UserEntity> onCompleteListener, final String userKey) {
        userRepository.getUser(onCompleteListener, userKey);
    }
}