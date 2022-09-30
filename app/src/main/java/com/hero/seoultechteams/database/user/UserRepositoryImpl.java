package com.hero.seoultechteams.database.user;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.data.user.local.UserLocalDataSource;
import com.hero.seoultechteams.data.user.remote.UserRemoteDataSource;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.domain.user.repository.UserRepository;
import com.hero.seoultechteams.utils.CacheManager;

import java.util.ArrayList;
import java.util.List;


public class UserRepositoryImpl implements UserRepository {

    private final UserRemoteDataSource userRemoteDataSource;
    private final UserLocalDataSource userLocalDataSource;

    public UserRepositoryImpl(UserRemoteDataSource userRemoteDataSource, UserLocalDataSource userLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userLocalDataSource = userLocalDataSource;
    }

    public void addUser(OnCompleteListener<UserEntity> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd) {
        userLocalDataSource.add(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, onFailedListener, userName, email, pwd);

        userRemoteDataSource.addUser(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, onFailedListener, userName, email, pwd);
    }

    public void updateUser(OnCompleteListener<UserEntity> onCompleteListener, UserEntity userEntity) {
        UserData userData = UserData.toData(userEntity);

        userLocalDataSource.update(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, userData);

        userRemoteDataSource.updateUser(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, userData);
    }

    @Override
    public void signOut() {
        userRemoteDataSource.signOut();
        userLocalDataSource.signOut();
    }

    public void getUser(final OnCompleteListener<UserEntity> onCompleteListener, final String userKey) {
        getUserFromCloud(onCompleteListener, userKey);
        // TODO: 2022-09-22 나중에 캐시 데이터를 통합하는 방법을 고려해보기
//        getUserFromCache(new OnCompleteListener<UserData>() {
//            @Override
//            public void onComplete(boolean isSuccess, UserData data) {
//                if (isSuccess && data != null) {
//                    onCompleteListener.onComplete(true, getUserEntity(data));
//                } else {
//                    getUserFromCloud(onCompleteListener, userKey);
//                }
//            }
//        }, userKey);
//        getUserFromCache(new OnCompleteListener<UserData>() {
//            @Override
//            public void onComplete(boolean isSuccess, UserData data) {
//                if (isSuccess) {
//                    if (data != null) {
//                        onCompleteListener.onComplete(true, data);
//                    } else {
//                        onCompleteListener.onComplete(true, null);
//                    }
//                } else {
////                    getUserFromLocal(onCompleteListener, data);
//                }
//            }
//        }, userKey);
    }

    @Override
    public void getUserListByName(OnCompleteListener<List<UserEntity>> onCompleteListener, String name) {
        getSearchedUserListByUserNameFromCloud(new OnCompleteListener<List<UserEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserEntity> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, name);
    }

    @Override
    public void getUserListByEmail(OnCompleteListener<List<UserEntity>> onCompleteListener, String email) {
        getSearchedUserListByUserEmailFromCloud(new OnCompleteListener<List<UserEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserEntity> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, email);
    }

    @Override
    public UserEntity getAccountProfile() {
        return userRemoteDataSource.getFirebaseAuthProfile().toEntity();
    }

    public void getUserFromCache(final OnCompleteListener<UserData> onCompleteListener, String userKey) {
        userLocalDataSource.getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userKey);
    }

    // TODO: 2022-09-30 getUserFromCache와 똑같은데 굳이 만들어줄 필요가 있는지???
    public void getUserFromLocal(final OnCompleteListener<UserData> onCompleteListener, String userKey) {
        userLocalDataSource.getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userKey);
    }

    public void getUserFromCloud(final OnCompleteListener<UserEntity> onCompleteListener, String userKey) {
        userRemoteDataSource.getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                UserEntity accountProfile = getAccountProfile();
                String userLocalProfileUri = getAccountProfile().getProfileImageUrl();
                if (userLocalProfileUri != null && !TextUtils.isEmpty(data.getProfileImageUrl())) {
                    String userLocalName = accountProfile.getName();
                    if (!userLocalProfileUri.toString().equals(data.getProfileImageUrl()) ||
                            !userLocalName.equals(data.getName())) {

                        updateLocalUser(data);
                    }
                }

                onCompleteListener.onComplete(isSuccess, getUserEntity(data));
            }
        }, userKey);
    }

    public void getSearchedUserListByUserNameFromCloud(final OnCompleteListener<List<UserEntity>> onCompleteListener, String userName) {
        userRemoteDataSource.getDataByUserName(new OnCompleteListener<List<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserData> data) {
                onCompleteListener.onComplete(isSuccess, getUserEntities(data));
            }
        }, userName);
    }

    public void getSearchedUserListByUserEmailFromCloud(final OnCompleteListener<List<UserEntity>> onCompleteListener, String userEmail) {
        userRemoteDataSource.getDataByUserEmail(new OnCompleteListener<List<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserData> data) {
                onCompleteListener.onComplete(isSuccess, getUserEntities(data));
            }
        }, userEmail);
    }

    public void addInvitedUser(final OnCompleteListener<ArrayList<UserData>> onCompleteListener,
                               TeamData teamData,
                               List<UserData> userDataList,
                               List<MemberData> memberDataList) {
        userRemoteDataSource.addUserList(new OnCompleteListener<List<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserData> data) {

            }
        }, teamData, userDataList, memberDataList);
    }

    public void updateLocalUser(UserData userData) {
        userRemoteDataSource.updateUser(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {

            }
        }, userData);
//        ((UserCloudStore)getCloudStore()).updateLocalUser(userData);
    }

    @NonNull
    private UserEntity getUserEntity(UserData data) {
        return new UserEntity(data.getName(), data.getEmail(), data.getProfileImageUrl(), data.getKey());
    }

    @NonNull
    private List<UserEntity> getUserEntities(List<UserData> data) {
        List<UserEntity> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).toEntity());
        }
        return result;
    }

    @Override
    public void addMemberListToTeam(OnCompleteListener<List<UserEntity>> onCompleteListener,
                                    TeamEntity teamEntity,
                                    List<UserEntity> inviteUserDataList,
                                    List<MemberEntity> teamMemberDataList) {

        List<UserData> userDataList = new ArrayList<>();
        List<MemberData> teamMemberList = new ArrayList<>();

        for (UserEntity userEntity : inviteUserDataList) {
            userDataList.add(UserData.toData(userEntity));
        }

        for (MemberEntity memberEntity : teamMemberDataList) {
            teamMemberList.add(MemberData.toData(memberEntity));
        }

        userRemoteDataSource.addUserList(new OnCompleteListener<List<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<UserData> data) {
                ArrayList<UserEntity> result = new ArrayList<>();

                for (UserData userData : data) {
                    result.add(userData.toEntity());
                }

                onCompleteListener.onComplete(isSuccess, result);
            }
        }, TeamData.toData(teamEntity), userDataList, teamMemberList);
    }
}