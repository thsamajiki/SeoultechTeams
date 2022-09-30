package com.hero.seoultechteams.view.main.team.contract;

import com.hero.seoultechteams.domain.team.entity.TeamEntity;

public abstract class TeamDetailContract {
    public interface View {
        void updatedTeamDetail(TeamEntity data);

        void failedUpdateTeamDetail();
    }

    public interface Presenter {
        void updateTeamDetail(String teamName, String teamDesc, TeamEntity data);
    }
}