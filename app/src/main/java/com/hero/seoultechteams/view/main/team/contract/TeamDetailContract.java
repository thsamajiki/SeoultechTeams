package com.hero.seoultechteams.view.main.team.contract;

import com.hero.seoultechteams.domain.team.entity.TeamEntity;

public abstract class TeamDetailContract {
    public interface View {
        void updatedTeamDetail(TeamEntity data);

        void failedUpdateTeamDetail();

        void onLoadTeam(TeamEntity data);

        void failedLoadTeam();
    }

    public interface Presenter {
        void updateTeamDetail(String teamName, String teamDesc);

        void requestTeamData(String teamKey);
    }
}