package com.hero.seoultechteams.database.team.datastore;

import android.content.Context;

import com.hero.seoultechteams.database.team.dao.TeamDao;
import com.hero.seoultechteams.database.team.database.AppTeamDatabase;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.utils.AppExecutors;

import java.util.List;

public class TeamLocalStore extends LocalStore<TeamData> {

    private AppTeamDatabase appTeamDatabase;
    private TeamDao teamDao;
    private static TeamLocalStore instance;
    private final AppExecutors appExecutors = new AppExecutors();

    private TeamLocalStore(Context context, TeamDao teamDao) {
        super(context);
        this.teamDao = teamDao;
    }

    private TeamLocalStore() {
    }

    public static TeamLocalStore getInstance(Context context, TeamDao teamDao) {
        if (instance == null) {
            instance = new TeamLocalStore(context, teamDao);
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String teamKey = params[0].toString();
                TeamData teamData = teamDao.getTeamFromKey(teamKey);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, teamData);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<TeamData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                List<TeamData> teamDataList = teamDao.getAllTeams();

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, teamDataList);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void add(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                teamDao.insertData(data);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, data);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void update(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                teamDao.updateData(data);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, data);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        if (data != null) {
            appExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    teamDao.deleteData(data);

                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (onCompleteListener != null) {
                                onCompleteListener.onComplete(true, data);
                            }
                        }
                    });
                }
            });
        }
    }
}