package com.hero.seoultechteams.database.team.datastore;

import android.content.Context;

import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.LocalDataStore;

import java.util.ArrayList;

public class TeamLocalStore extends LocalDataStore<TeamData> {

    private static TeamLocalStore instance;

    public TeamLocalStore(Context context) {
        super(context);
    }

    private TeamLocalStore() {
    }

    public static TeamLocalStore getInstance() {
        if (instance == null) {
            instance = new TeamLocalStore();
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TeamData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        String teamKey = params[0].toString();
//        TeamData teamData = getAppDataBase().getUserDao().getUserFromKey(teamKey);
//        onCompleteListener.onComplete(true, teamData);
    }

    @Override
    public void getDataList(OnCompleteListener<ArrayList<TeamData>> onCompleteListener, Object... params) {

    }

    @Override
    public void add(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {

    }

    @Override
    public void update(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {

    }

    @Override
    public void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {

    }
}