package com.hero.seoultechteams.database.team.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.team.database.AppTeamDatabase;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.LocalStore;

import java.util.List;

public class TeamLocalStore extends LocalStore<TeamData> {

    private AppTeamDatabase appTeamDatabase;
    private static TeamLocalStore instance;

    public TeamLocalStore(Context context, AppTeamDatabase appTeamDatabase) {
        super(context);
        this.appTeamDatabase = appTeamDatabase;
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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String teamKey = params[0].toString();
                TeamData teamData = getTeamDatabase().getTeamDao().getTeamFromKey(teamKey);
                onCompleteListener.onComplete(true, teamData);
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<TeamData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        List<TeamData> teamDataList = getTeamDatabase().getTeamDao().getAllTeams();
        onCompleteListener.onComplete(true, teamDataList);
    }

    public AppTeamDatabase getTeamDatabase() {
        return appTeamDatabase;
    }

    @Override
    public void add(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        getTeamDatabase().getTeamDao().insertData(data);
    }

    @Override
    public void update(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        getTeamDatabase().getTeamDao().updateData(data);
    }

    @Override
    public void remove(OnCompleteListener<TeamData> onCompleteListener, TeamData data) {
        if (data != null) {
            getTeamDatabase().getTeamDao().deleteData(data);
        }
    }
}