package com.hero.seoultechteams.domain.member.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

import java.util.List;

public class GetUserListByNameUseCase {
    private UserRepository userRepository;

    public GetUserListByNameUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<List<UserEntity>> onCompleteListener, String name) {
        userRepository.getUserListByName(onCompleteListener, name);
    }
}
