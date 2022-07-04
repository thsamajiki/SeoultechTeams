package com.hero.seoultechteams.database.team;

import android.content.Context;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.Repository;
import com.hero.seoultechteams.database.team.datastore.TeamCacheStore;
import com.hero.seoultechteams.database.team.datastore.TeamCloudStore;
import com.hero.seoultechteams.database.team.datastore.TeamLocalStore;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.LocalDataStore;

import java.util.ArrayList;

public class TeamRepository extends Repository<TeamData> {

    public TeamRepository(Context context) {
        super(context);
    }

    @Override
    protected CloudStore<TeamData> createCloudStore(Context context) {
        return new TeamCloudStore(context);
    }

    @Override
    protected LocalDataStore<TeamData> createLocalStore(Context context) {
        return new TeamLocalStore(context);
    }

    @Override
    protected CacheStore<TeamData> createCacheStore() {
        return TeamCacheStore.getInstance();
    }

    public void addTeam(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData) {
        getCloudStore().add(onCompleteListener, teamData);
    }

    public void updateTeam(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData) {
        getCloudStore().update(onCompleteListener, teamData);
    }

    public void removeTeam(OnCompleteListener<TeamData> onCompleteListener, TeamData teamData) {
        getCloudStore().remove(onCompleteListener, teamData);
    }

    public void getTeam(final OnCompleteListener<TeamData> onCompleteListener, final String teamKey) {
        getTeamFromCache(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData teamData) {
                if (isSuccess && teamData != null) {
                    onCompleteListener.onComplete(true, teamData);
                } else {
                    getTeamFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    public void getTeamList(final OnCompleteListener<ArrayList<TeamData>> onCompleteListener) {
        getCacheStore().getDataList(new OnCompleteListener<ArrayList<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TeamData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    getCloudStore().getDataList(onCompleteListener);
                }
            }
        });
    }

    public void getTeamFromCache(final OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        getCacheStore().getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData teamData) {
                onCompleteListener.onComplete(isSuccess, teamData);
            }
        }, teamKey);
    }

    public void getTeamFromCloud(final OnCompleteListener<TeamData> onCompleteListener, String teamKey) {
        getCloudStore().getData(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData teamData) {
                onCompleteListener.onComplete(isSuccess, teamData);
            }
        }, teamKey);
    }
}