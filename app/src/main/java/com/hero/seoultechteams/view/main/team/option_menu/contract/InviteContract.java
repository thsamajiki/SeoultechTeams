package com.hero.seoultechteams.view.main.team.option_menu.contract;

import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class InviteContract {
    public interface View {
        void onAddNewMemberList(TeamEntity teamData, ArrayList<UserEntity> inviteUserDataList, List<MemberEntity> teamMemberDataList);

        void failedAddNewMemberList();

        void onGetUserListByName(List<UserEntity> data);

        void failedGetUserListByName();

        void onGetUserListByEmail(List<UserEntity> data);

        void failedGetUserListByEmail();
    }

    public interface Presenter {
        void inviteUserList(TeamEntity teamData, ArrayList<UserEntity> inviteUserDataList, List<MemberEntity> teamMemberDataList);

        void getUserList(String keyword);
    }
}