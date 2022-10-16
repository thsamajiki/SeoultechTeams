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

        teamRemoteDataSource.add(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData remoteData) {
                if (isSuccess) {
                    teamLocalDataSource.add(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData localData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity());
                            } else {
                                // Remote는 add 성공함. isSuccess == false 로컬 실패
                                // 그렇더라도, Remote는 성공했으니까 전체적으로 성공이라고 볼 수 있지 않을까
                                onCompleteListener.onComplete(true, remoteData.toEntity());
                            }
                        }
                    }, teamData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamData);
    }

    public void updateTeam(OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity) {
        TeamData teamData = TeamData.toData(teamEntity);

        teamRemoteDataSource.update(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData remoteData) {
                if (isSuccess) {
                    teamLocalDataSource.update(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData localData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity());
                            } else {
                                onCompleteListener.onComplete(true, remoteData.toEntity());
                            }
                        }
                    }, teamData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamData);
    }

    public void removeTeam(OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity) {
        TeamData teamData = TeamData.toData(teamEntity);

        teamRemoteDataSource.remove(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData remoteData) {
                if (isSuccess) {
                    teamLocalDataSource.remove(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData localData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity());
                            } else {
                                onCompleteListener.onComplete(true, remoteData.toEntity());
                            }
                        }
                    }, teamData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamData);
    }

    public void getTeam(final OnCompleteListener<TeamEntity> onCompleteListener, final String teamKey) {
        getTeamFromLocal(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData localData) {
                if (isSuccess && localData != null) {
                    onCompleteListener.onComplete(true, getTeamEntity(localData));
                } else {
                    getTeamFromRemote(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData remoteData) {
                            onCompleteListener.onComplete(isSuccess, remoteData.toEntity());
                        }
                    }, teamKey);
                }
            }
        }, teamKey);
    }

    public void getTeamList(final OnCompleteListener<List<TeamEntity>> onCompleteListener, final String userKey) {
        getTeamListFromLocal(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getTeamEntities(data));
                } else {
                    getTeamListFromRemote(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    private void getTeamFromLocal(final OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        teamLocalDataSource.getData(onCompleteListener, teamKey);
    }

    private void getTeamFromRemote(final OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        teamRemoteDataSource.getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamKey);
    }

    private void getTeamListFromLocal(final OnCompleteListener<List<TeamData>> onCompleteListener, String userKey) {
        teamLocalDataSource.getDataList(onCompleteListener, userKey);
    }

    private void getTeamListFromRemote(final OnCompleteListener<List<TeamEntity>> onCompleteListener, String userKey) {
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