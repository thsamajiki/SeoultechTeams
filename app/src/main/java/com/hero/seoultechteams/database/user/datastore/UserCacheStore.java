package com.hero.seoultechteams.database.user.datastore;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class UserCacheStore extends CacheStore<UserData> {

    private static UserCacheStore instance;
    private UserData userData;
    public static final String TYPE_MY_TEAM = "My";

    private UserCacheStore() {
    }

    public static UserCacheStore getInstance() {
        if (instance == null) {
            instance = new UserCacheStore();
        }
        return instance;
    }

    // 팀의 멤버 1명의 정보를 불러오는 메소드
    // 팀의 멤버 1명의 평가 데이터(승인률, 성실도 등등)에 관한 정보를 불러오는 메소드
    @Override
    public void getData(OnCompleteListener<UserData> onCompleteListener, Object... params) {
        if (params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        String userKey = params[0].toString();
        if (getDataList() != null && getDataList().size() > 0) {
            for (UserData userData : getDataList()) {
                if (userData.getKey().equals(userKey)) {
                    onCompleteListener.onComplete(true, userData);
                    return;
                }
            }
        }
        onCompleteListener.onComplete(true, null);
    }

    // 팀에서 멤버 목록을 불러오는 메소드
    @Override
    public void getDataList(OnCompleteListener<List<UserData>> onCompleteListener, Object... params) {
        String type = params[0].toString();
        String key = params[1].toString();
        if (type.equals(TYPE_MY_TEAM)) {
            getTeamMemberList(onCompleteListener, key);
        } else {
            throw new IllegalArgumentException("정의되지 않은 타입입니다.");
        }
    }

    private void getTeamMemberList(OnCompleteListener<List<UserData>> onCompleteListener, String userKey) {
        List<UserData> userDataList = getDataList();
        if (userDataList.isEmpty()) {
            onCompleteListener.onComplete(true, null);
        } else {
            List<UserData> teamMemberList = new ArrayList<>();
            for (UserData userData: teamMemberList) {
                if (userData.getKey().equals(userKey)) {
                    userDataList.add(userData);
                }
            }
            onCompleteListener.onComplete(true, null);
        }
    }

    @Override
    public void add(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        getDataList().add(data);
    }

    public void addUserListToTeam(final OnCompleteListener<List<UserData>> onCompleteListener, final TeamData teamData, List<UserData> userDataList, List<MemberData> memberDataList) {
        onCompleteListener.onComplete(true, userDataList);
    }

    @Override
    public void update(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().set(originIndex, data);
        }
    }

    @Override
    public void remove(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().remove(originIndex);
        }
    }
}