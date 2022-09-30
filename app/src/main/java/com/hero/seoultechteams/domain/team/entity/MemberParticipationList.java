package com.hero.seoultechteams.domain.team.entity;

import com.hero.seoultechteams.view.main.team.option_menu.MemberParticipation;

import java.util.List;

public class MemberParticipationList {

    private final List<MemberParticipation> memberFaithfulRanking;
    private final MemberParticipation memberPerformanceKing;
    private final MemberParticipation memberLatestKing;

    public MemberParticipationList(
            List<MemberParticipation> memberFaithfulRanking,
            MemberParticipation memberPerformanceKing,
            MemberParticipation memberLatestKing)
    {
        this.memberFaithfulRanking = memberFaithfulRanking;
        this.memberPerformanceKing = memberPerformanceKing;
        this.memberLatestKing = memberLatestKing;
    }

    public List<MemberParticipation> getMemberFaithfulRanking() {
        return memberFaithfulRanking;
    }

    public MemberParticipation getMemberPerformanceKing() {
        return memberPerformanceKing;
    }

    public MemberParticipation getMemberLatestKing() {
        return memberLatestKing;
    }
}
