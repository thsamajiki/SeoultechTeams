package com.hero.seoultechteams.domain.member.repository;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;

import java.util.ArrayList;
import java.util.List;

public interface MemberRepository {
    void getMemberList(final OnCompleteListener<List<MemberEntity>> onCompleteListener, final String teamKey);

    void addMemberListToTeam(final OnCompleteListener<TeamEntity> onCompleteListener, List<UserData> inviteUserDataList, List<MemberData> teamMemberDataList);
}
