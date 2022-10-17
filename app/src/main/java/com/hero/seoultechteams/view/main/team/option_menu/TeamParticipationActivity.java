package com.hero.seoultechteams.view.main.team.option_menu;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityTeamParticipationBinding;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.main.team.option_menu.contract.TeamParticipationContract;
import com.hero.seoultechteams.view.main.team.option_menu.presenter.TeamParticipationPresenter;

import java.util.List;


public class TeamParticipationActivity extends AppCompatActivity implements View.OnClickListener, TeamParticipationContract.View {

//    private ImageView ivBack, ivBestIcon, ivLatestIcon;
//    private CircleImageView ivMemberFaithFulFirstRank, ivMemberFaithFulSecondRank, ivMemberFaithFulThirdRank, ivMemberFaithFulFourthRank,
//            ivMemberLateKing, ivMemberBestPerformance;
//    private TextView tvFaithfulRankingTitle, tvFaithfulFirstRank, tvFaithfulSecondRank, tvFaithfulThirdRank, tvFaithfulFourthRank,
//            tvFaithfulFirstRankMemberName, tvFaithfulSecondRankMemberName, tvFaithfulThirdRankMemberName, tvFaithfulFourthRankMemberName,
//            tvFaithfulFirstRankPercent, tvFaithfulSecondRankPercent, tvFaithfulThirdRankPercent, tvFaithfulFourthRankPercent,
//            tvLatestKingTitle, tvLatestKingMemberName, tvLatestKingPercent, tvPerformanceKingTitle, tvPerformanceKingMemberName, tvPerformanceKingPercent;
//    private ArrayList<MemberEntity> teamMemberDataList = new ArrayList<>();
//    private ArrayList<TodoEntity> teamTodoDataList = new ArrayList<>();


    private ActivityTeamParticipationBinding binding;

    private final TeamParticipationContract.Presenter presenter = new TeamParticipationPresenter(this,
            Injector.getInstance().provideGetMemberParticipationUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamParticipationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        initView();
        setOnClickListener();
        presenter.getMemberParticipationList(getTeamData().getTeamKey());
    }


    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
    }

    private TeamEntity getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    // 멤버 우리팀 영웅 (가장 승인율이 높은 사람)
    @Override
    public void showMemberPerformanceKing(MemberParticipation memberParticipation) {
        MemberEntity bestMemberData = memberParticipation.getMemberData();
        ParticipationData participationData = memberParticipation.getParticipationData();

        if (TextUtils.isEmpty(bestMemberData.getProfileImageUrl())) {
            Glide.with(this).load(R.drawable.ic_user).into(binding.ivMemberBestPerformance);
        } else {
            Glide.with(this).load(bestMemberData.getProfileImageUrl()).into(binding.ivMemberBestPerformance);
        }
        binding.tvPerformanceKingMemberName.setText(bestMemberData.getName());
        binding.tvPerformanceKingPercent.setText(String.valueOf(participationData.getMemberTodoPerformancePercent()) + '%');
    }

    // 멤버 우리팀 지연왕 (가장 지연이 많은 사람)
    @Override
    public void showMemberLatestKing(MemberParticipation memberParticipation) {
        MemberEntity latestMemberData = memberParticipation.getMemberData();
        ParticipationData participationData = memberParticipation.getParticipationData();

        if (TextUtils.isEmpty(latestMemberData.getProfileImageUrl())) {
            Glide.with(this).load(R.drawable.ic_user).into(binding.ivMemberLatestKing);
        } else {
            Glide.with(this).load(latestMemberData.getProfileImageUrl()).into(binding.ivMemberLatestKing);
        }
        binding.tvLatestKingMemberName.setText(latestMemberData.getName());
        binding.tvLatestKingPercent.setText(String.valueOf(participationData.getMemberTodoLatePercent()) + '%');
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void showTeamParticipationRanking(List<MemberParticipation> data) {
        for(int i = 0; i < data.size(); i++) {
            MemberEntity memberData = data.get(i).getMemberData();
            ParticipationData participationData = data.get(i).getParticipationData();
            if (i == 0) {
                setTeamParticipationInfo(memberData, participationData, binding.ivMemberFaithfulFirstRank, binding.tvFaithfulFirstRankMemberName, binding.tvFaithfulFirstRankPercent);
            } else if (i == 1) {
                setTeamParticipationInfo(memberData, participationData, binding.ivMemberFaithfulSecondRank, binding.tvFaithfulSecondRankMemberName, binding.tvFaithfulSecondRankPercent);
            } else if (i == 2) {
                setTeamParticipationInfo(memberData, participationData, binding.ivMemberFaithfulThirdRank, binding.tvFaithfulThirdRankMemberName, binding.tvFaithfulThirdRankPercent);
            } else if (i == 3) {
                setTeamParticipationInfo(memberData, participationData, binding.ivMemberFaithfulFourthRank, binding.tvFaithfulFourthRankMemberName, binding.tvFaithfulFourthRankPercent);
            }
        }
    }

    private void setTeamParticipationInfo(MemberEntity memberData,
                           ParticipationData participationData,
                           ImageView profileImageView,
                           TextView name,
                           TextView percent) {
        if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
            Glide.with(this).load(R.drawable.ic_user).into(profileImageView);
        } else {
            Glide.with(this).load(memberData.getProfileImageUrl()).into(profileImageView);
        }
        name.setText(memberData.getName());

        percent.setText(String.valueOf(participationData.getMemberTodoFaithfulPercent()) + '%');
    }

    @Override
    public void failedShowTeamParticipationRanking() {
        Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }
}