package com.hero.seoultechteams.view.main.team.option_menu;

import com.hero.seoultechteams.database.member.entity.MemberData;

public class MemberParticipation {
    private MemberData memberData;
    private ParticipationData participationData;

    public MemberData getMemberData() {
        return memberData;
    }

    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
    }

    public ParticipationData getParticipationData() {
        return participationData;
    }

    public void setParticipationData(ParticipationData participationData) {
        this.participationData = participationData;
    }
}
