package com.hero.seoultechteams.view.main.team.contract;

import com.hero.seoultechteams.domain.team.entity.TeamEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TeamListContract {
    public interface View {
        void onGetTeamList(List<TeamEntity> data);

        void onEmptyTeamList();

        void failedGetTeamList();
    }

    public interface Presenter {
        void getTeamListFromDatabase();
    }
}