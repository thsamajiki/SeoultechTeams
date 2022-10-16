package com.hero.seoultechteams.view.main.team.option_menu.presenter;

import android.text.TextUtils;
import android.util.Patterns;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.usecase.InviteNewMemberListUseCase;
import com.hero.seoultechteams.domain.member.usecase.GetUserListByEmailUseCase;
import com.hero.seoultechteams.domain.member.usecase.GetUserListByNameUseCase;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.main.team.option_menu.contract.InviteContract;

import java.util.ArrayList;
import java.util.List;

public class InvitePresenter implements InviteContract.Presenter {
    private final InviteContract.View view;
    private final InviteNewMemberListUseCase inviteNewMemberListUseCase;
    private final GetUserListByNameUseCase getUserListByNameUseCase;
    private final GetUserListByEmailUseCase getUserListByEmailUseCase;

    private final List<MemberEntity> teamMemberList;

    public InvitePresenter(InviteContract.View view,
                           InviteNewMemberListUseCase inviteNewMemberListUseCase,
                           GetUserListByNameUseCase getUserListByNameUseCase,
                           GetUserListByEmailUseCase getUserListByEmailUseCase,
                           List<MemberEntity> teamMemberList) {
        this.view = view;
        this.inviteNewMemberListUseCase = inviteNewMemberListUseCase;
        this.getUserListByNameUseCase = getUserListByNameUseCase;
        this.getUserListByEmailUseCase = getUserListByEmailUseCase;
        this.teamMemberList = teamMemberList;
    }

    @Override
    public void inviteUserList(TeamEntity teamData, ArrayList<UserEntity> inviteUserDataList, List<MemberEntity> teamMemberDataList) {
        inviteNewMemberListUseCase.invoke(new OnCompleteListener<List<UserEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserEntity> data) {
                if (isSuccess && data != null) {
                    view.onAddNewMemberList(teamData, inviteUserDataList, teamMemberDataList);
                } else {
                    view.failedAddNewMemberList();
                }
            }
        }, teamData, inviteUserDataList, teamMemberDataList);
    }

    @Override
    public void getUserList(String input) {
        if (checkEmailValid(input)) {
            getUserListByEmail(input);
        } else {
            getUserListByName(input);
        }
    }

    private void getUserListByName(String userName) {
        getUserListByNameUseCase.invoke(new OnCompleteListener<List<UserEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserEntity> data) {
                List<UserEntity> result = new ArrayList<>();

                for (UserEntity userEntity : data) {
                    if (isNotMember(userEntity)) {
                        result.add(userEntity);
                    }
                }

                if (isSuccess && !result.isEmpty()) {
                    view.onGetUserListByName(data);
                } else {
                    view.failedGetUserListByName();
                }
            }
        }, userName);
    }

    private void getUserListByEmail(String userEmail) {
        getUserListByEmailUseCase.invoke(new OnCompleteListener<List<UserEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserEntity> data) {
                List<UserEntity> result = new ArrayList<>();

                for (UserEntity userEntity : data) {
                    if (isNotMember(userEntity)) {
                        result.add(userEntity);
                    }
                }

                if (isSuccess && !result.isEmpty()) {
                    view.onGetUserListByEmail(data);
                } else {
                    view.failedGetUserListByEmail();
                }
            }
        }, userEmail);
    }

    private boolean isNotMember(UserEntity data) {
        return !isMember(data);
    }

    private boolean isMember(UserEntity data) {
        for (MemberEntity memberData : teamMemberList) {
            if (data.getKey().equals(memberData.getKey())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
