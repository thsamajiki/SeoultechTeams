package com.hero.seoultechteams.domain.member.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class GetUserListByEmailUseCase {
    private UserRepository userRepository;

    public GetUserListByEmailUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<List<UserEntity>> onCompleteListener, String email) {
        userRepository.getUserListByEmail(onCompleteListener, email);
    }
}
