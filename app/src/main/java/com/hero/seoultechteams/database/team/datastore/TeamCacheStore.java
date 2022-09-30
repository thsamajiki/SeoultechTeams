package com.hero.seoultechteams.database.team.datastore;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.ArrayList;
import java.util.List;

public class TeamCacheStore extends CacheStore<TeamData> {

    private static TeamCacheStore instance;

    private TeamCacheStore() {
    }

    public static TeamCacheStore getInstance() {
        if (instance == null) {
            instance = new TeamCacheStore();
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, Object... params) {

    }

    @Override
    public void getDataList(OnCompleteListener<List<TeamData>> onCompleteListener, Object... params) {
        if (getDataList().size() == 0) {
            onCompleteListener.onComplete(true, null);
        } else {
            onCompleteListener.onComplete(false, getDataList());
        }
    }

    @Override
    public void add(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        getDataList().add(data);
    }

    @Override
    public void update(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().set(originIndex, data);
        }
    }

    @Override
    public void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().remove(originIndex);
        }
    }
}