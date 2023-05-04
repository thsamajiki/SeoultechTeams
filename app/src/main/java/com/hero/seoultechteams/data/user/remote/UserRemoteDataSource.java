package com.hero.seoultechteams.data.user.remote;

import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;

import java.util.List;

public interface UserRemoteDataSource {
    void addUser(OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd);
    void updateUser(OnCompleteListener<UserData> onCompleteListener, UserData userData);
    void removeUser(OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, UserData userData);

    void addUserList(OnCompleteListener<List<UserData>> onCompleteListener, TeamData teamData, List<UserData> userDataList, List<MemberData> memberDataList);

    void getData(OnCompleteListener<UserData> onCompleteListener, String userKey);
    void getDataByUserName(OnCompleteListener<List<UserData>> onCompleteListener, String userName);
    void getDataByUserEmail(OnCompleteListener<List<UserData>> onCompleteListener, String userEmail);

    UserData getFirebaseAuthProfile();

    void signOut();
}
