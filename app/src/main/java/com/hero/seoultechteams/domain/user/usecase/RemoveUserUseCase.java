package com.hero.seoultechteams.domain.user.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

public class RemoveUserUseCase {
    private final UserRepository userRepository;

    public RemoveUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<UserEntity> onCompleteListener, OnFailedListener onFailedListener, UserEntity userEntity) {
        userRepository.removeUser(onCompleteListener, onFailedListener, userEntity);
    }
}
