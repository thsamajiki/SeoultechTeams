package com.hero.seoultechteams.domain.user.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

public class UpdateUserUseCase {
    private final UserRepository userRepository;

    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<UserEntity> onCompleteListener, UserEntity userEntity) {
        userRepository.updateUser(onCompleteListener, userEntity);
    }
}
