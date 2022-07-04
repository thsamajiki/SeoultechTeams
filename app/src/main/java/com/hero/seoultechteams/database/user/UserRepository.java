package com.hero.seoultechteams.database.user;

import android.content.Context;
import android.util.Log;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.LocalDataStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.Repository;
import com.hero.seoultechteams.database.member.datastore.MemberCloudStore;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.datastore.UserCacheStore;
import com.hero.seoultechteams.database.user.datastore.UserCloudStore;
import com.hero.seoultechteams.database.user.datastore.UserLocalStore;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;


public class UserRepository extends Repository<UserData> {

    public UserRepository(Context context) {
        super(context);
    }

    @Override
    protected CloudStore<UserData> createCloudStore(Context context) {
        return new UserCloudStore(context);
    }

    @Override
    protected LocalDataStore<UserData> createLocalStore(Context context) {
        return new UserLocalStore(context);
    }

    @Override
    protected CacheStore<UserData> createCacheStore() {
        return UserCacheStore.getInstance();
    }

    public void addUser(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        getCloudStore().add(onCompleteListener, userData);
    }

    public void updateUser(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        getCloudStore().update(onCompleteListener, userData);
    }

    public void getUser(final OnCompleteListener<UserData> onCompleteListener, final String userKey) {
        getUserFromCache(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    getUserFromCloud(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    public void getUserFromCache(final OnCompleteListener<UserData> onCompleteListener, String userKey) {
        getCacheStore().getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userKey);
    }

    public void getUserFromCloud(final OnCompleteListener<UserData> onCompleteListener, String userKey) {
        getCloudStore().getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userKey);
    }

    public void getSearchedUserListByUserName(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userName) {
        getSearchedUserListByUserNameFromCloud(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userName);
    }

    public void getSearchedUserListByUserEmail(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userEmail) {
        getSearchedUserListByUserEmailFromCloud(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userEmail);
    }

    public void getSearchedUserListByUserNameFromCloud(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userName) {
        ((UserCloudStore)getCloudStore()).getDataByUserName(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userName);
    }

    public void getSearchedUserListByUserEmailFromCloud(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userEmail) {
        ((UserCloudStore)getCloudStore()).getDataByUserEmail(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userEmail);
    }

    public void addInvitedUser(final OnCompleteListener<ArrayList<UserData>> onCompleteListener, TeamData teamData, ArrayList<UserData> userDataList, ArrayList<MemberData> memberDataList) {
        ((UserCloudStore)getCloudStore()).addUserListToTeam(onCompleteListener, teamData, userDataList, memberDataList);
        Log.d("qwer7", "UserRepository-addInvitedUser : 초대하기 전 팀원들 수 : " + memberDataList.size());
        for (MemberData memberData : memberDataList) {
            Log.d("qwer8", "UserRepository-addInvitedUser : 초대하기 전 팀원의 이름 : " + memberData.getName());
        }
    }

    public void updateLocalUser(UserData userData) {
        ((UserCloudStore)getCloudStore()).updateLocalUser(userData);
    }
}