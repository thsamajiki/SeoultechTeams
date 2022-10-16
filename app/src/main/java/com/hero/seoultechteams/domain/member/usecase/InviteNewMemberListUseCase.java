package com.hero.seoultechteams.domain.member.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class InviteNewMemberListUseCase {
    private UserRepository userRepository;

    public InviteNewMemberListUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void invoke(final OnCompleteListener<List<UserEntity>> onCompleteListener,
                       TeamEntity teamEntity,
                       ArrayList<UserEntity> inviteUserDataList,
                       List<MemberEntity> teamMemberDataList) {
        userRepository.addMemberListToTeam(onCompleteListener, teamEntity, inviteUserDataList, teamMemberDataList);
    }
}