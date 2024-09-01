package com.hero.seoultechteams.team;

import static org.junit.Assert.assertEquals;

import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.view.main.team.option_menu.MemberParticipation;
import com.hero.seoultechteams.view.main.team.option_menu.ParticipationData;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MemberParticipationUnitTest {
    // 멤버 우리팀 영웅 (가장 승인율이 높은 사람)
    @Test
    public void getMemberPerformanceKing() {
        MemberParticipation member1Participation = getMemberParticipationData(10, 2, 3, 12, 3,
                "김철수",
                "chulsoo@TechOne.com",
                "null",
                "techone",
                "1");
        MemberParticipation member2Participation = getMemberParticipationData(6, 8, 2, 9, 3,
                "이은희",
                "eunhee@TechOne.com",
                "null",
                "techone",
                "2");
        MemberParticipation member3Participation = getMemberParticipationData(9, 5, 4, 7, 11,
                "박광온",
                "gwangon@TechOne.com",
                "null",
                "techone",
                "3");

        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        memberParticipationList.add(member1Participation);
        memberParticipationList.add(member2Participation);
        memberParticipationList.add(member3Participation);

        Collections.sort(memberParticipationList, new Comparator<MemberParticipation>() {
            @Override
            public int compare(MemberParticipation o1, MemberParticipation o2) {
                return Integer.compare(o2.getParticipationData().getMemberTodoPerformancePercent(), o1.getParticipationData().getMemberTodoPerformancePercent());
            }
        });

        assertEquals(member1Participation, memberParticipationList.get(0));
    }

    // 멤버 우리팀 지연왕 (가장 지연이 많은 사람)
    @Test
    public void getMemberLatestKing() {
        MemberParticipation member1Participation = getMemberParticipationData(10, 2, 3, 12, 3,
                "김철수",
                "chulsoo@TechOne.com",
                "null",
                "techone",
                "1");
        MemberParticipation member2Participation = getMemberParticipationData(6, 8, 2, 9, 3, "이은희",
                "eunhee@TechOne.com",
                "null",
                "techone",
                "2");
        MemberParticipation member3Participation = getMemberParticipationData(9, 5, 4, 7, 11,
                "박광온",
                "gwangon@TechOne.com",
                "null",
                "techone",
                "3");

        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        memberParticipationList.add(member1Participation);
        memberParticipationList.add(member2Participation);
        memberParticipationList.add(member3Participation);

        Collections.sort(memberParticipationList, new Comparator<MemberParticipation>() {
            @Override
            public int compare(MemberParticipation o1, MemberParticipation o2) {
                return Integer.compare(o2.getParticipationData().getMemberTodoLatePercent(), o1.getParticipationData().getMemberTodoLatePercent());
            }
        });

        assertEquals(member2Participation, memberParticipationList.get(0));
    }

    private MemberEntity getMemberData(String name, String email, String profileImageUrl, String teamKey, String key) {
        return new MemberEntity(
                name,
                email,
                profileImageUrl,
                teamKey,
                key
        );
    }

    private MemberParticipation getMemberParticipationData(int memberCountTodoRegularSubmitted, int memberCountTodoLateSubmitted, int memberCountTodoResubmitted,
                                                                  int memberCountTodoConfirmed, int memberCountTodoDismissed,
                                                                  String name, String email, String profileImageUrl, String teamKey, String key) {
        // 성실도, 지연율, 성과도 연산
        int memberTodoFaithfulPercent = (int)(((double)(memberCountTodoRegularSubmitted) / (double)(memberCountTodoRegularSubmitted + memberCountTodoLateSubmitted)) * 100);
        int memberTodoLatePercent = (int)(((double)(memberCountTodoLateSubmitted) / (double)(memberCountTodoRegularSubmitted + memberCountTodoLateSubmitted)) * 100);
        int memberTodoPerformancePercent = (int)(((double)(memberCountTodoConfirmed) / (double)(memberCountTodoRegularSubmitted + memberCountTodoLateSubmitted + memberCountTodoResubmitted)) * 100);

        ParticipationData participationData = new ParticipationData();
        participationData.setCountTodoRegularSubmitted(memberCountTodoRegularSubmitted);
        participationData.setCountTodoLateSubmitted(memberCountTodoLateSubmitted);
        participationData.setCountTodoResubmitted(memberCountTodoResubmitted);
        participationData.setCountTodoConfirmed(memberCountTodoConfirmed);
        participationData.setCountTodoDismissed(memberCountTodoDismissed);
        participationData.setMemberTodoFaithfulPercent(memberTodoFaithfulPercent);
        participationData.setMemberTodoPerformancePercent(memberTodoPerformancePercent);
        participationData.setMemberTodoLatePercent(memberTodoLatePercent);


        MemberParticipation memberParticipation = new MemberParticipation();
        memberParticipation.setMemberData(getMemberData(name, email, profileImageUrl, teamKey, key));
        memberParticipation.setParticipationData(participationData);

        return memberParticipation;
    }
}