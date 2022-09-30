package com.hero.seoultechteams.view.main.team.contract;

import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;

public abstract class CreateTeamContract {
    public interface View {
        void addedTeamList(TeamEntity data);

        void failedAddTeam();
    }

    public interface Presenter {
        void addTeamToDatabase(String teamName, String teamDesc);
    }
}