package com.hero.seoultechteams.domain.user.repository;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository {
    void getUser(final OnCompleteListener<UserEntity> onCompleteListener, final String userKey);

    void getUserListByName(final OnCompleteListener<List<UserEntity>> onCompleteListener, String name);

    void getUserListByEmail(final OnCompleteListener<List<UserEntity>> onCompleteListener, String email);

    UserEntity getAccountProfile();

    void addUser(final OnCompleteListener<UserEntity> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd);

    void updateUser(final OnCompleteListener<UserEntity> onCompleteListener, UserEntity userEntity);

    void signOut();

    void addMemberListToTeam(final OnCompleteListener<List<UserEntity>> onCompleteListener, TeamEntity teamEntity, List<UserEntity> inviteUserDataList, List<MemberEntity> teamMemberDataList);
}