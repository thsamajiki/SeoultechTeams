package com.hero.seoultechteams.database.member;

import android.content.Context;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.LocalDataStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.Repository;
import com.hero.seoultechteams.database.member.datastore.MemberCacheStore;
import com.hero.seoultechteams.database.member.datastore.MemberCloudStore;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;

public class MemberRepository extends Repository<MemberData> {

    private boolean refresh = false;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public MemberRepository(Context context) {
        super(context);
    }

    @Override
    protected CloudStore<MemberData> createCloudStore(Context context) {
        return new MemberCloudStore(context);
    }

    @Override
    protected LocalDataStore<MemberData> createLocalStore(Context context) {
        return null;
    }

    @Override
    protected CacheStore<MemberData> createCacheStore() {
        return MemberCacheStore.getInstance();
    }

    public void addMemberToTeam(OnCompleteListener<ArrayList<MemberData>> onCompleteListener, TeamData teamData, ArrayList<UserData> userDataList, ArrayList<MemberData> memberDataList) {
        ((MemberCloudStore)getCloudStore()).addNewMemberList(onCompleteListener, teamData, userDataList, memberDataList);
    }

    public void updateMemberParticipation(OnCompleteListener<MemberData> onCompleteListener, MemberData memberData) {
        getCloudStore().update(onCompleteListener, memberData);
    }

    public void getMemberList(final OnCompleteListener<ArrayList<MemberData>> onCompleteListener, final String teamKey) {
        if (isRefresh()) {
            getMemberFromCloud(onCompleteListener, teamKey);
            setRefresh(false);
            return;
        }
        getCacheStore().getDataList(new OnCompleteListener<ArrayList<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    getMemberFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    private void getMemberFromCloud(final OnCompleteListener<ArrayList<MemberData>> onCompleteListener, String teamKey) {
        getCloudStore().getDataList(new OnCompleteListener<ArrayList<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberData> memberData) {
                if (isSuccess && isNotEmptyList(memberData)) {
                    onCompleteListener.onComplete(true, memberData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamKey);
    }
}