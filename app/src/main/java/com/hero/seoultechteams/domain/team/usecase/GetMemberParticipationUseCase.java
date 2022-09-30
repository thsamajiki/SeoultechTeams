package com.hero.seoultechteams.domain.team.usecase;

import static com.hero.seoultechteams.database.todo.entity.Event.EVENT_CONFIRM;
import static com.hero.seoultechteams.database.todo.entity.Event.EVENT_DISMISS;
import static com.hero.seoultechteams.database.todo.entity.Event.EVENT_SUBMIT;

import android.util.Log;

import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.member.usecase.GetMemberListUseCase;
import com.hero.seoultechteams.domain.team.entity.MemberParticipationList;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.usecase.GetTeamTodoListUseCase;
import com.hero.seoultechteams.view.main.team.option_menu.MemberParticipation;
import com.hero.seoultechteams.view.main.team.option_menu.ParticipationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GetMemberParticipationUseCase {

    private final GetTeamTodoListUseCase getTeamTodoListUseCase;
    private final GetMemberListUseCase getMemberListUseCase;

    public GetMemberParticipationUseCase(GetTeamTodoListUseCase getTeamTodoListUseCase, GetMemberListUseCase getMemberListUseCase) {
        this.getTeamTodoListUseCase = getTeamTodoListUseCase;
        this.getMemberListUseCase = getMemberListUseCase;
    }

    private List<MemberEntity> memberDataList;
    private List<TodoEntity> todoDataList;

    public void invoke(final OnCompleteListener<MemberParticipationList> onCompleteListener, String teamKey) {
        memberDataList = null;
        todoDataList = null;

        getMemberListUseCase.invoke(new OnCompleteListener<ArrayList<MemberEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberEntity> teamMemberDataList) {
                memberDataList = teamMemberDataList;

                if (todoDataList != null) {
                    makeMemberParticipationList(todoDataList, teamMemberDataList, onCompleteListener);
                }
            }
        }, teamKey);

        getTeamTodoListUseCase.invoke(new OnCompleteListener<ArrayList<TodoEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoEntity> teamTodoDataList) {
                todoDataList = teamTodoDataList;
                if (memberDataList != null) {
                    makeMemberParticipationList(teamTodoDataList, memberDataList, onCompleteListener);
                }
            }
        }, teamKey);
    }

    private void makeMemberParticipationList(List<TodoEntity> teamTodoDataList,
                                             List<MemberEntity> teamMemberDataList,
                                             final OnCompleteListener<MemberParticipationList> onCompleteListener) {
        if (teamTodoDataList == null) {
            Log.e("empty", "emptyTodoDataList");
        } else {
            HashMap<MemberEntity, ArrayList<TodoEntity>> memberTodoDataHashMap = new HashMap<>();
            HashMap<MemberEntity, ParticipationData> memberParticipationDataHashMap = new HashMap<>();

            ArrayList<Event> todoEventList = new ArrayList<>();

            for (MemberEntity memberData : teamMemberDataList) {
                for (TodoEntity todoEntity : teamTodoDataList) {
                    if (todoEntity.getUserKey().equals(memberData.getKey())) {
                        ArrayList<TodoEntity> todoEntityList = memberTodoDataHashMap.get(memberData);
                        if (todoEntityList == null) {
                            todoEntityList = new ArrayList<>();
                        }
                        todoEntityList.add(todoEntity);
                        memberTodoDataHashMap.put(memberData, todoEntityList);
                    }
                }

                int countTodoRegularSubmitted = 0;
                int countTodoLateSubmitted = 0;
                int countTodoResubmitted = 0;
                int countTotalSubmitted = 0;
                int countTodoConfirmed = 0;
                int countTodoDismissed = 0;
                int memberTodoFaithfulPercent = 0;
                int memberTodoPerformancePercent = 0;
                int memberTodoLatePercent = 0;

                if (memberTodoDataHashMap.get(memberData) != null) {
                    for (TodoEntity todoEntity : memberTodoDataHashMap.get(memberData)) {
                        boolean isFirstSubmit = true;
                        for (Event event : todoEntity.getEventHistory()) {
                            switch (event.getEvent()) {
                                case EVENT_SUBMIT:
                                    if (isFirstSubmit) {
                                        if (event.getTime() <= todoEntity.getTodoEndTime()) {
                                            countTodoRegularSubmitted++;
                                        } else {
                                            countTodoLateSubmitted++;
                                        }
                                        isFirstSubmit = false;
                                    } else {
                                        countTodoResubmitted++;
                                    }
                                    break;
                                case EVENT_DISMISS:
                                    countTodoDismissed++;
                                    break;
                                case EVENT_CONFIRM:
                                    countTodoConfirmed++;
                                    break;
                            }
                        }
                    }
                    countTotalSubmitted = countTodoRegularSubmitted + countTodoLateSubmitted + countTodoResubmitted;
                }

                if (countTodoRegularSubmitted + countTodoLateSubmitted > 0) {
                    memberTodoFaithfulPercent = (int)(((double)(countTodoRegularSubmitted) / (double)(countTodoRegularSubmitted + countTodoLateSubmitted)) * 100);
                    memberTodoLatePercent = (int)(((double)(countTodoLateSubmitted) / (double)(countTodoRegularSubmitted + countTodoLateSubmitted)) * 100);;
                    memberTodoPerformancePercent = (int)(((double)(countTodoConfirmed) / (double)(countTodoRegularSubmitted + countTodoLateSubmitted + countTodoResubmitted)) * 100);
                } else {
                    memberTodoFaithfulPercent = 0;
                    memberTodoLatePercent = 0;
                    memberTodoPerformancePercent = 0;
                }

                ParticipationData participationData = new ParticipationData();
                participationData.setCountTodoRegularSubmitted(countTotalSubmitted);
                participationData.setCountTodoConfirmed(countTodoConfirmed);
                participationData.setCountTodoDismissed(countTodoDismissed);
                participationData.setMemberTodoFaithfulPercent(memberTodoFaithfulPercent);
                participationData.setMemberTodoPerformancePercent(memberTodoPerformancePercent);
                participationData.setMemberTodoLatePercent(memberTodoLatePercent);


                memberParticipationDataHashMap.put(memberData, participationData);

                List<MemberParticipation> memberFaithfulRanking = getMemberFaithfulRanking(memberParticipationDataHashMap);
                MemberParticipation memberPerformanceKing = getMemberPerformanceKing(memberParticipationDataHashMap);
                MemberParticipation memberLatestKing = getMemberLatestKing(memberParticipationDataHashMap);

                MemberParticipationList memberParticipationList = new MemberParticipationList(memberFaithfulRanking, memberPerformanceKing, memberLatestKing);
                onCompleteListener.onComplete(true, memberParticipationList);
            }
        }
    }

    // 멤버 성실도 랭킹 (지연되지 않고 (정상) 제출을 가장 많이 한 사람)
    private List<MemberParticipation> getMemberFaithfulRanking(HashMap<MemberEntity, ParticipationData> memberParticipationDataHashMap) {
        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        for (MemberEntity memberData : memberParticipationDataHashMap.keySet()) {
            MemberParticipation memberParticipation = new MemberParticipation();
            memberParticipation.setMemberData(memberData);
            memberParticipation.setParticipationData(memberParticipationDataHashMap.get(memberData));
            memberParticipationList.add(memberParticipation);
        }

        Collections.sort(memberParticipationList, new Comparator<MemberParticipation>() {
            @Override
            public int compare(MemberParticipation o1, MemberParticipation o2) {
                return Integer.compare(o2.getParticipationData().getMemberTodoFaithfulPercent(), o1.getParticipationData().getMemberTodoFaithfulPercent());
            }
        });

        return memberParticipationList;
    }

    // 멤버 우리팀 영웅 (가장 승인율이 높은 사람)
    private MemberParticipation getMemberPerformanceKing(HashMap<MemberEntity, ParticipationData> memberParticipationDataHashMap) {
        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        for (MemberEntity memberData : memberParticipationDataHashMap.keySet()) {
            MemberParticipation memberParticipation = new MemberParticipation();
            memberParticipation.setMemberData(memberData);
            memberParticipation.setParticipationData(memberParticipationDataHashMap.get(memberData));
            memberParticipationList.add(memberParticipation);
        }

        Collections.sort(memberParticipationList, new Comparator<MemberParticipation>() {
            @Override
            public int compare(MemberParticipation o1, MemberParticipation o2) {
                return Integer.compare(o2.getParticipationData().getMemberTodoPerformancePercent(), o1.getParticipationData().getMemberTodoPerformancePercent());
            }
        });

        return memberParticipationList.get(0);
    }

    // 멤버 우리팀 지연왕 (가장 지연이 많은 사람)
    private MemberParticipation getMemberLatestKing(HashMap<MemberEntity, ParticipationData> memberParticipationDataHashMap) {
        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        for (MemberEntity memberData : memberParticipationDataHashMap.keySet()) {
            MemberParticipation memberParticipation = new MemberParticipation();
            memberParticipation.setMemberData(memberData);
            memberParticipation.setParticipationData(memberParticipationDataHashMap.get(memberData));
            memberParticipationList.add(memberParticipation);
        }

        Collections.sort(memberParticipationList, new Comparator<MemberParticipation>() {
            @Override
            public int compare(MemberParticipation o1, MemberParticipation o2) {
                return Integer.compare(o2.getParticipationData().getMemberTodoLatePercent(), o1.getParticipationData().getMemberTodoLatePercent());
            }
        });

        return memberParticipationList.get(0);
    }
}
