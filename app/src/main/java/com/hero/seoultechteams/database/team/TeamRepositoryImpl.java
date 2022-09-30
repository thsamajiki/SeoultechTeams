package com.hero.seoultechteams.database.team;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.data.team.local.TeamLocalDataSource;
import com.hero.seoultechteams.data.team.remote.TeamRemoteDataSource;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

public class TeamRepositoryImpl implements TeamRepository {

    private final TeamRemoteDataSource teamRemoteDataSource;
    private final TeamLocalDataSource teamLocalDataSource;

    public TeamRepositoryImpl(TeamRemoteDataSource teamRemoteDataSource, TeamLocalDataSource teamLocalDataSource) {
        this.teamRemoteDataSource = teamRemoteDataSource;
        this.teamLocalDataSource = teamLocalDataSource;
    }

    public void addTeam(OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity) {
        TeamData teamData = TeamData.toData(teamEntity);

        teamLocalDataSource.add(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, teamData);

        teamRemoteDataSource.add(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, teamData);
    }

    public void updateTeam(OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity) {
        TeamData teamData = TeamData.toData(teamEntity);

        teamLocalDataSource.update(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, teamData);

        teamRemoteDataSource.update(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, teamData);
    }

    public void removeTeam(OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity) {
        TeamData teamData = TeamData.toData(teamEntity);

        teamLocalDataSource.remove(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, teamData);

        teamRemoteDataSource.remove(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, teamData);
    }

    public void getTeam(final OnCompleteListener<TeamEntity> onCompleteListener, final String teamKey) {
        getTeamFromCache(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getTeamEntity(data));
                } else {
//                    getTeamFromLocal(onCompleteListener, teamKey);
                    getTeamFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    public void getTeamList(final OnCompleteListener<List<TeamEntity>> onCompleteListener, final String userKey) {
        getTeamListFromCache(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getTeamEntities(data));
                } else {
//                    getTeamListFromLocal(onCompleteListener, userKey);
                    getTeamListFromCloud(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    public void getTeamFromCache(final OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        teamLocalDataSource.getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, teamKey);
    }

    public void getTeamFromLocal(final OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        teamLocalDataSource.getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, teamKey);
    }

    public void getTeamFromCloud(final OnCompleteListener<TeamEntity> onCompleteListener, String teamKey) {
        teamRemoteDataSource.getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, getTeamEntity(data));
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamKey);
    }

    public void getTeamListFromCache(final OnCompleteListener<List<TeamData>> onCompleteListener, String userKey) {
        teamLocalDataSource.getDataList(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, userKey);
    }

    public void getTeamListFromLocal(final OnCompleteListener<List<TeamData>> onCompleteListener, String userKey) {
        teamLocalDataSource.getDataList(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> data) {
//                if (isSuccess && data != null) {
//                    onCompleteListener.onComplete(true, data);
//                } else {
//                    getTeamListFromCloud(onCompleteListener, userKey);
//                }
            }
        }, userKey);
    }

    public void getTeamListFromCloud(final OnCompleteListener<List<TeamEntity>> onCompleteListener, String userKey) {
        teamRemoteDataSource.getDataList(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, getTeamEntities(data));
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, userKey);
    }

    @NonNull
    private TeamEntity getTeamEntity(TeamData data) {
        return new TeamEntity(data.getTeamName(), data.getTeamDesc(), data.getLeaderKey(), data.getCreatedDate(), data.getTeamKey());
    }

    @NonNull
    private List<TeamEntity> getTeamEntities(List<TeamData> data) {
        List<TeamEntity> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).toEntity());
        }
        return result;
    }
}