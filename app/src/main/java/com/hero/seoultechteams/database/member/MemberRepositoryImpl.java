package com.hero.seoultechteams.database.member;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.data.member.local.MemberLocalDataSource;
import com.hero.seoultechteams.data.member.remote.MemberRemoteDataSource;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.repository.MemberRepository;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;

import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepository {

    private final MemberRemoteDataSource memberRemoteDataSource;
    private final MemberLocalDataSource memberLocalDataSource;

    private boolean refresh = false;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public MemberRepositoryImpl(MemberRemoteDataSource memberRemoteDataSource, MemberLocalDataSource memberLocalDataSource) {
        this.memberRemoteDataSource = memberRemoteDataSource;
        this.memberLocalDataSource = memberLocalDataSource;
    }

    public void getMemberList(final OnCompleteListener<List<MemberEntity>> onCompleteListener, final String teamKey) {
        if (isRefresh()) {
            getMemberListFromCloud(onCompleteListener, teamKey);
            setRefresh(false);
            return;
        }

        memberLocalDataSource.getDataList(new OnCompleteListener<List<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getMemberEntities(data));
                } else {
                    getMemberListFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    @Override
    public void addMemberListToTeam(OnCompleteListener<TeamEntity> onCompleteListener, List<UserData> inviteUserDataList, List<MemberData> teamMemberDataList) {

    }

    private void getMemberListFromCache(final OnCompleteListener<List<MemberEntity>> onCompleteListener, String teamKey) {
        memberLocalDataSource.getDataList(new OnCompleteListener<List<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberData> data) {
                if (isSuccess) {
                } else {
                    getMemberListFromLocal(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    private void getMemberListFromLocal(final OnCompleteListener<List<MemberEntity>> onCompleteListener, String teamKey) {
        memberLocalDataSource.getDataList(new OnCompleteListener<List<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, getMemberEntities(data));
                } else {
                    getMemberListFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    private void getMemberListFromCloud(final OnCompleteListener<List<MemberEntity>> onCompleteListener, String teamKey) {
        memberRemoteDataSource.getDataList(new OnCompleteListener<List<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getMemberEntities(data));
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamKey);
    }

    @NonNull
    private List<MemberEntity> getMemberEntities(List<MemberData> data) {
        List<MemberEntity> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).toEntity());
        }
        return result;
    }
}