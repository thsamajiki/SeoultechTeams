package com.hero.seoultechteams.data.team.local;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.ArrayList;
import java.util.List;

public interface TeamLocalDataSource {
    // CacheStore
    void getData(OnCompleteListener<TeamData> onCompleteListener, String teamKey);

    void getDataList(OnCompleteListener<List<TeamData>> arrayListOnCompleteListener, String userKey);

    // LocalStore
    void add(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData);

    void update(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData);

    void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData);
}
