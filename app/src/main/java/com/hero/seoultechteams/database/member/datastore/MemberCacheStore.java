package com.hero.seoultechteams.database.member.datastore;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.ArrayList;
import java.util.List;

public class MemberCacheStore extends CacheStore<MemberData> {

    private static MemberCacheStore instance;

    private MemberCacheStore() {
    }

    public static MemberCacheStore getInstance() {
        if (instance == null) {
            instance = new MemberCacheStore();
        }
        return instance;
    }

    // 팀의 멤버 1명의 정보를 불러오는 메소드
    // 팀의 멤버 1명의 평가 데이터(승인률, 성실도 등등)에 관한 정보를 불러오는 메소드
    @Override
    public void getData(OnCompleteListener<MemberData> onCompleteListener, Object... params) {
        if (params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }
        String memberKey = params[0].toString();
        if (getDataList() != null && getDataList().size() > 0) {
            for (MemberData memberData : getDataList()) {
                if (memberData.getKey().equals(memberKey)) {
                    onCompleteListener.onComplete(true, memberData);
                    return;
                }
            }
        }
        onCompleteListener.onComplete(true, null);
    }

    // 팀에서 멤버 목록을 불러오는 메소드
    @Override
    public void getDataList(OnCompleteListener<List<MemberData>> onCompleteListener, Object... params) {
        String key = params[0].toString();
        getTeamMemberList(onCompleteListener, key);
    }

    private void getTeamMemberList(OnCompleteListener<List<MemberData>> onCompleteListener, String teamKey) {
        List<MemberData> memberDataList = getDataList();
        if (memberDataList.isEmpty()) {
            onCompleteListener.onComplete(true, null);
        } else {
            List<MemberData> teamMemberList = new ArrayList<>();
            for (MemberData memberData: teamMemberList) {
                if (memberData.getTeamKey().equals(teamKey)) {
                    memberDataList.add(memberData);
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
    public void add(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        getDataList().add(data);
    }

    @Override
    public void update(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().set(originIndex, data);
        }
    }

    @Override
    public void remove(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().remove(originIndex);
        }
    }
}