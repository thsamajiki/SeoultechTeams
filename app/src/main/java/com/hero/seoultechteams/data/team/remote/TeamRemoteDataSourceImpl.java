package com.hero.seoultechteams.data.team.remote;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.datastore.TeamCloudStore;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.ArrayList;
import java.util.List;

public class TeamRemoteDataSourceImpl implements TeamRemoteDataSource {
    private final TeamCloudStore teamCloudStore;

    public TeamRemoteDataSourceImpl(TeamCloudStore teamCloudStore) {
        this.teamCloudStore = teamCloudStore;
    }

    @Override
    public void add(OnCompleteListener<TeamData> onCompleteListener, TeamData teamEntity) {
        teamCloudStore.add(onCompleteListener, teamEntity);
    }

    @Override
    public void update(OnCompleteListener<TeamData> onCompleteListener, TeamData teamEntity) {
        teamCloudStore.update(onCompleteListener, teamEntity);
    }

    @Override
    public void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData teamEntity) {
        teamCloudStore.remove(onCompleteListener, teamEntity);
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        teamCloudStore.getData(onCompleteListener, teamKey);
    }

    @Override
    public void getDataList(OnCompleteListener<List<TeamData>> onCompleteListener, String userKey) {
        teamCloudStore.getDataList(onCompleteListener, userKey);
    }
}
