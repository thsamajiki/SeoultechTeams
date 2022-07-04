package com.hero.seoultechteams.database.user.datastore;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;

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
    public void getDataList(OnCompleteListener<ArrayList<UserData>> onCompleteListener, Object... params) {
        String type = params[0].toString();
        String key = params[1].toString();
        if (type.equals(TYPE_MY_TEAM)) {
            getTeamMemberList(onCompleteListener, key);
        } else {
            throw new IllegalArgumentException("정의되지 않은 타입입니다.");
        }
    }

    private void getTeamMemberList(OnCompleteListener<ArrayList<UserData>> onCompleteListener, String userKey) {
        ArrayList<UserData> userDataList = getDataList();
        if (userDataList.isEmpty()) {
            onCompleteListener.onComplete(true, null);
        } else {
            ArrayList<UserData> teamMemberList = new ArrayList<>();
            for (UserData userData: teamMemberList) {
                if (userData.getKey().equals(userKey)) {
                    userDataList.add(userData);
                }
            }
            if (teamMemberList.isEmpty()) {
                onCompleteListener.onComplete(true, null);
            } else {
                onCompleteListener.onComplete(true, teamMemberList);
            }
        }
    }

    @Override
    public void add(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        getDataList().add(data);
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