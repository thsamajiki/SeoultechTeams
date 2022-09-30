package com.hero.seoultechteams.data.team.remote;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.ArrayList;
import java.util.List;

public interface TeamRemoteDataSource {
    void add(OnCompleteListener<TeamData> onCompleteListener, TeamData teamEntity);

    void update(OnCompleteListener<TeamData> onCompleteListener, TeamData teamEntity);

    void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData teamEntity);

    void getData(OnCompleteListener<TeamData> onCompleteListener, String teamKey);

    void getDataList(OnCompleteListener<List<TeamData>> arrayListOnCompleteListener, String teamKey);
}
