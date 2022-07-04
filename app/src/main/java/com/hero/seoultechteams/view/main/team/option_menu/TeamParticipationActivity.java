package com.hero.seoultechteams.view.main.team.option_menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.MemberRepository;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.TodoRepository;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hero.seoultechteams.database.todo.entity.Event.EVENT_CONFIRM;
import static com.hero.seoultechteams.database.todo.entity.Event.EVENT_DISMISS;
import static com.hero.seoultechteams.database.todo.entity.Event.EVENT_SUBMIT;
import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TEAM_MEMBER_LIST;


public class TeamParticipationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack, ivBestIcon, ivLatestIcon;
    private CircleImageView ivMemberFaithFulFirstRank, ivMemberFaithFulSecondRank, ivMemberFaithFulThirdRank, ivMemberFaithFulFourthRank,
            ivMemberLateKing, ivMemberBestPerformance;
    private TextView tvFaithfulRankingTitle, tvFaithfulFirstRank, tvFaithfulSecondRank, tvFaithfulThirdRank, tvFaithfulFourthRank,
            tvFaithfulFirstRankMemberName, tvFaithfulSecondRankMemberName, tvFaithfulThirdRankMemberName, tvFaithfulFourthRankMemberName,
            tvFaithfulFirstRankPercent, tvFaithfulSecondRankPercent, tvFaithfulThirdRankPercent, tvFaithfulFourthRankPercent,
            tvLatestKingTitle, tvLatestKingMemberName, tvLatestKingPercent, tvPerformanceKingTitle, tvPerformanceKingMemberName, tvPerformanceKingPercent;
    private ArrayList<MemberData> teamMemberDataList = new ArrayList<>();
    private ArrayList<TodoData> teamTodoDataList = new ArrayList<>();

    private int countTodoRegularSubmitted;
    private int countTodoLateSubmitted;
    private int countTodoResubmitted;
    private int countTotalSubmitted;
    private int countTodoConfirmed;
    private int countTodoDismissed;

    private int memberTodoFaithfulPercent;
    private int memberTodoPerformancePercent;
    private int memberTodoLatePercent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_participation);
        initView();
        setOnClickListener();
        getMemberDataListFromDatabase();
        getTodoDataListFromDatabase();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        ivMemberFaithFulFirstRank = findViewById(R.id.iv_member_faithful_first_rank);
        ivMemberFaithFulSecondRank = findViewById(R.id.iv_member_faithful_second_rank);
        ivMemberFaithFulThirdRank = findViewById(R.id.iv_member_faithful_third_rank);
        ivMemberFaithFulFourthRank = findViewById(R.id.iv_member_faithful_fourth_rank);
        ivMemberLateKing = findViewById(R.id.iv_member_latest_king);
        ivMemberBestPerformance = findViewById(R.id.iv_member_best_performance);

        tvFaithfulRankingTitle = findViewById(R.id.tv_faithful_ranking_title);
        tvFaithfulFirstRank = findViewById(R.id.tv_faithful_first_rank);
        tvFaithfulSecondRank = findViewById(R.id.tv_faithful_second_rank);
        tvFaithfulThirdRank = findViewById(R.id.tv_faithful_third_rank);
        tvFaithfulFourthRank = findViewById(R.id.tv_faithful_fourth_rank);

        tvFaithfulFirstRankMemberName = findViewById(R.id.tv_faithful_first_rank_member_name);
        tvFaithfulSecondRankMemberName = findViewById(R.id.tv_faithful_second_rank_member_name);
        tvFaithfulThirdRankMemberName = findViewById(R.id.tv_faithful_third_rank_member_name);
        tvFaithfulFourthRankMemberName = findViewById(R.id.tv_faithful_fourth_rank_member_name);

        tvFaithfulFirstRankPercent = findViewById(R.id.tv_faithful_first_rank_percent);
        tvFaithfulSecondRankPercent = findViewById(R.id.tv_faithful_second_rank_percent);
        tvFaithfulThirdRankPercent = findViewById(R.id.tv_faithful_third_rank_percent);
        tvFaithfulFourthRankPercent = findViewById(R.id.tv_faithful_fourth_rank_percent);


        tvPerformanceKingTitle = findViewById(R.id.tv_performance_king_title);
        ivBestIcon = findViewById(R.id.iv_best_icon);
        tvPerformanceKingMemberName = findViewById(R.id.tv_performance_king_member_name);
        tvPerformanceKingPercent = findViewById(R.id.tv_performance_king_percent);
        tvLatestKingTitle = findViewById(R.id.tv_latest_king_title);
        ivLatestIcon = findViewById(R.id.iv_latest_icon);
        tvLatestKingMemberName = findViewById(R.id.tv_latest_king_member_name);
        tvLatestKingPercent = findViewById(R.id.tv_latest_king_percent);
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
    }

    private void getMemberDataListFromDatabase() {
        MemberRepository memberRepository = new MemberRepository(this);
        String teamKey = getTeamData().getTeamKey();
        memberRepository.getMemberList(new OnCompleteListener<ArrayList<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
                if (isSuccess && data != null) {
                    teamMemberDataList.addAll(data);
                } else {
                    Toast.makeText(TeamParticipationActivity.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, teamKey);
    }

    private TeamData getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    private ArrayList<MemberData> getTeamMemberDataList() {
        return getIntent().getParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST);
    }

    private void getTodoDataListFromDatabase() {
        TodoRepository todoRepository = new TodoRepository(this);
        todoRepository.getTeamTodoList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                if (isSuccess) {
                    teamTodoDataList = data;
                    if (teamTodoDataList == null) {
                        Toast.makeText(TeamParticipationActivity.this, "등록된 할 일이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<MemberData, ArrayList<TodoData>> memberTodoDataHashMap = new HashMap<>();
                        HashMap<MemberData, ParticipationData> memberParticipationDataHashMap = new HashMap<>();

                        ArrayList<Event> todoEventList = new ArrayList<>();

                        for (MemberData memberData : teamMemberDataList) {
                            for (TodoData todoData : teamTodoDataList) {
                                if (todoData.getUserKey().equals(memberData.getKey())) {
                                    ArrayList<TodoData> todoDataList = memberTodoDataHashMap.get(memberData);
                                    if (todoDataList == null) {
                                        todoDataList = new ArrayList<>();
                                    }
                                    todoDataList.add(todoData);
                                    memberTodoDataHashMap.put(memberData, todoDataList);
                                }
                            }

                            countTodoRegularSubmitted = 0;
                            countTodoLateSubmitted = 0;
                            countTodoResubmitted = 0;
                            countTotalSubmitted = 0;
                            countTodoConfirmed = 0;
                            countTodoDismissed = 0;
                            memberTodoFaithfulPercent = 0;
                            memberTodoPerformancePercent = 0;
                            memberTodoLatePercent = 0;

                            if (memberTodoDataHashMap.get(memberData) != null) {
                                for (TodoData todoData : memberTodoDataHashMap.get(memberData)) {
                                    boolean isFirstSubmit = true;
                                    for (Event event : todoData.getEventHistory()) {
                                        switch (event.getEvent()) {
                                            case EVENT_SUBMIT:
                                                if (isFirstSubmit) {
                                                    if (event.getTime() <= todoData.getTodoEndTime()) {
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

                            showMemberFaithfulRanking(memberParticipationDataHashMap);
                            showMemberPerformanceKing(memberParticipationDataHashMap);
                            showMemberLatestKing(memberParticipationDataHashMap);
                        }
                    }
                } else {
                    Toast.makeText(TeamParticipationActivity.this, "할 일을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, getTeamData().getTeamKey());
    }

    // 멤버 성실도 랭킹 (지연되지 않고 (정상) 제출을 가장 많이 한 사람)
    private void showMemberFaithfulRanking(HashMap<MemberData, ParticipationData> memberParticipationDataHashMap) {
        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        for (MemberData memberData : memberParticipationDataHashMap.keySet()) {
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

        for(int i = 0; i < memberParticipationList.size(); i++) {
            MemberData memberData = memberParticipationList.get(i).getMemberData();
            ParticipationData participationData = memberParticipationList.get(i).getParticipationData();
            if (i == 0) {
                if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
                    Glide.with(this).load(R.drawable.ic_user).into(ivMemberFaithFulFirstRank);
                } else {
                    Glide.with(this).load(memberData.getProfileImageUrl()).into(ivMemberFaithFulFirstRank);
                }
                tvFaithfulFirstRankMemberName.setText(memberData.getName());
                tvFaithfulFirstRankPercent.setText(String.valueOf(participationData.getMemberTodoFaithfulPercent()) + '%');
            } else if (i == 1) {
                if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
                    Glide.with(this).load(R.drawable.ic_user).into(ivMemberFaithFulSecondRank);
                } else {
                    Glide.with(this).load(memberData.getProfileImageUrl()).into(ivMemberFaithFulSecondRank);
                }
                tvFaithfulSecondRankMemberName.setText(memberData.getName());
                tvFaithfulSecondRankPercent.setText(String.valueOf(participationData.getMemberTodoFaithfulPercent()) + '%');
            } else if (i == 2) {
                if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
                    Glide.with(this).load(R.drawable.ic_user).into(ivMemberFaithFulThirdRank);
                } else {
                    Glide.with(this).load(memberData.getProfileImageUrl()).into(ivMemberFaithFulThirdRank);
                }
                tvFaithfulThirdRankMemberName.setText(memberData.getName());
                tvFaithfulThirdRankPercent.setText(String.valueOf(participationData.getMemberTodoFaithfulPercent()) + '%');
            } else if (i == 3) {
                if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
                    Glide.with(this).load(R.drawable.ic_user).into(ivMemberFaithFulFourthRank);
                } else {
                    Glide.with(this).load(memberData.getProfileImageUrl()).into(ivMemberFaithFulFourthRank);
                }
                tvFaithfulFourthRankMemberName.setText(memberData.getName());
                tvFaithfulFourthRankPercent.setText(String.valueOf(participationData.getMemberTodoFaithfulPercent()) + '%');
            }
        }
    }

    // 멤버 우리팀 영웅 (가장 승인율이 높은 사람)
    private void showMemberPerformanceKing(HashMap<MemberData, ParticipationData> memberParticipationDataHashMap) {
        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        for (MemberData memberData : memberParticipationDataHashMap.keySet()) {
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

        for(int i = 0; i < memberParticipationList.size(); i++) {
            MemberData bestMemberData = memberParticipationList.get(i).getMemberData();
            ParticipationData participationData = memberParticipationList.get(i).getParticipationData();
            if (i == 0) {
                if (TextUtils.isEmpty(bestMemberData.getProfileImageUrl())) {
                    Glide.with(this).load(R.drawable.ic_user).into(ivMemberBestPerformance);
                } else {
                    Glide.with(this).load(bestMemberData.getProfileImageUrl()).into(ivMemberBestPerformance);
                }
                tvPerformanceKingMemberName.setText(bestMemberData.getName());
                tvPerformanceKingPercent.setText(String.valueOf(participationData.getMemberTodoPerformancePercent()) + '%');
            }
        }
    }

    // 멤버 우리팀 지연왕 (가장 지연이 많은 사람)
    private void showMemberLatestKing(HashMap<MemberData, ParticipationData> memberParticipationDataHashMap) {
        ArrayList<MemberParticipation> memberParticipationList = new ArrayList<>();
        for (MemberData memberData : memberParticipationDataHashMap.keySet()) {
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

        for(int i = 0; i < memberParticipationList.size(); i++) {
            MemberData latestMemberData = memberParticipationList.get(i).getMemberData();
            ParticipationData participationData = memberParticipationList.get(i).getParticipationData();
            if (i == 0) {
                if (TextUtils.isEmpty(latestMemberData.getProfileImageUrl())) {
                    Glide.with(this).load(R.drawable.ic_user).into(ivMemberLateKing);
                } else {
                    Glide.with(this).load(latestMemberData.getProfileImageUrl()).into(ivMemberLateKing);
                }
                tvLatestKingMemberName.setText(latestMemberData.getName());
                tvLatestKingPercent.setText(String.valueOf(participationData.getMemberTodoLatePercent()) + '%');
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}