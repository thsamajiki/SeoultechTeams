package com.hero.seoultechteams.data.team.local;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.datastore.TeamCacheStore;
import com.hero.seoultechteams.database.team.datastore.TeamLocalStore;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.List;

public class TeamLocalDataSourceImpl implements TeamLocalDataSource {
    private final TeamLocalStore teamLocalStore;
    private final TeamCacheStore teamCacheStore;

    public TeamLocalDataSourceImpl(TeamLocalStore teamLocalStore, TeamCacheStore teamCacheStore) {
        this.teamLocalStore = teamLocalStore;
        this.teamCacheStore = teamCacheStore;
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        teamCacheStore.getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    teamLocalStore.getData(new OnCompleteListener<TeamData>() {
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
            }
        }, teamKey);
    }

    @Override
    public void getDataList(OnCompleteListener<List<TeamData>> onCompleteListener, String userKey) {
        teamCacheStore.getDataList(new OnCompleteListener<List<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TeamData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    teamLocalStore.getDataList(new OnCompleteListener<List<TeamData>>() {
                        @Override
                        public void onComplete(boolean isSuccess, List<TeamData> data) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, data);
                            } else {
                                onCompleteListener.onComplete(false, null);
                            }
                        }
                    });
                }
            }
        }, userKey);
    }

    @Override
    public void add(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData) {
        teamLocalStore.add(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData localData) {
                if (isSuccess) {
                    teamCacheStore.add(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData cacheData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, cacheData);
                            } else {
                                onCompleteListener.onComplete(true, localData);
                            }
                        }
                    }, teamData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamData);
    }

    @Override
    public void update(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData) {
        teamLocalStore.update(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData localData) {
                if (isSuccess) {
                    teamCacheStore.update(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData cacheData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, cacheData);
                            } else {
                                onCompleteListener.onComplete(true, localData);
                            }
                        }
                    }, teamData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamData);
    }

    @Override
    public void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData) {
        teamLocalStore.remove(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData localData) {
                if (isSuccess) {
                    teamCacheStore.remove(new OnCompleteListener<TeamData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TeamData cacheData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, cacheData);
                            } else {
                                onCompleteListener.onComplete(true, localData);
                            }
                        }
                    }, teamData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, teamData);
    }
}