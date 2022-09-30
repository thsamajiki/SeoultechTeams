package com.hero.seoultechteams.view.main.team.option_menu;

import com.hero.seoultechteams.domain.member.entity.MemberEntity;

public class MemberParticipation {
    private MemberEntity memberData;
    private ParticipationData participationData;

    public MemberEntity getMemberData() {
        return memberData;
    }

    public void setMemberData(MemberEntity memberData) {
        this.memberData = memberData;
    }

    public ParticipationData getParticipationData() {
        return participationData;
    }

    public void setParticipationData(ParticipationData participationData) {
        this.participationData = participationData;
    }
}
