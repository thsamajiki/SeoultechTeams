package com.hero.seoultechteams.view.main.team.option_menu.contract;

import com.hero.seoultechteams.view.main.team.option_menu.MemberParticipation;

import java.util.List;

public abstract class TeamParticipationContract {
    public interface View {
        void showTeamParticipationRanking(List<MemberParticipation> data);

        void showMemberPerformanceKing(MemberParticipation memberParticipation);

        void showMemberLatestKing(MemberParticipation memberParticipation);

        void failedShowTeamParticipationRanking();
    }

    public interface Presenter {
        void getMemberParticipationList(String teamKey);
    }
}