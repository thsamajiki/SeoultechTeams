package com.hero.seoultechteams.view.main.team.option_menu;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.option_menu.InviteActivity.EXTRA_INVITE_USER;
import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TEAM_MEMBER_LIST;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityTeamMemberListBinding;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.contract.TeamMemberListContract;
import com.hero.seoultechteams.view.main.team.option_menu.presenter.TeamMemberListPresenter;

import java.util.ArrayList;
import java.util.List;


public class TeamMemberListActivity extends BaseActivity implements View.OnClickListener, OnRecyclerItemClickListener<MemberEntity>, TeamMemberListContract.View {

    private ActivityTeamMemberListBinding binding;
    private final ArrayList<MemberEntity> teamMemberDataList = new ArrayList<>();
    private TeamMemberListAdapter teamMemberListAdapter;
    private ActivityResultLauncher<Intent> photoResultLauncher;

    private final TeamMemberListContract.Presenter presenter = new TeamMemberListPresenter(this,
            Injector.getInstance().provideGetMemberListUseCase());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamMemberListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initTeamMemberListRecyclerViewAdapter();
        presenter.getMemberDataListFromDatabase(getTeamData(), teamMemberDataList);
        setOnClickListeners();
    }


    private void initTeamMemberListRecyclerViewAdapter() {
        teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberDataList);
        teamMemberListAdapter.memberCallBack(new TeamMemberListAdapter.OnMemberProfileImageClickListener() {
            @Override
            public void profileImageOnClick(String profileImageUrl) {
            }
        });
        teamMemberListAdapter.setLeaderKey(getTeamData().getLeaderKey());
        teamMemberListAdapter.setOnRecyclerItemClickListener(this);
        binding.rvTeamMemberList.setAdapter(teamMemberListAdapter);
    }

    private void setOnClickListeners() {
        binding.ivBack.setOnClickListener(this);
        binding.btnGoToInvite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_member_profile:
                //intentMemberProfilePhoto(getTeamData().getUserKey());
                break;
            case R.id.btn_go_to_invite:
                intentInviteActivity();
                break;
        }
    }

    private TeamEntity getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    private ArrayList<MemberEntity> getTeamMemberDataList() {
        return getIntent().getParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST);
    }

    private void intentInviteActivity() {
        Intent intent = new Intent(this, InviteActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
        intent.putParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST, teamMemberDataList);
        inviteResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent>
            inviteResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        List<UserEntity> inviteUserDataList = data.getParcelableArrayListExtra(EXTRA_INVITE_USER);
                        List<MemberEntity> newMemberDataList = new ArrayList<>();
                        MemberEntity newMemberData = new MemberEntity();
                        for (UserEntity userData : inviteUserDataList) {
                            newMemberData.setKey(userData.getKey());
                            newMemberData.setName(userData.getName());
                            newMemberData.setEmail(userData.getEmail());
                            newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
                            newMemberData.setTeamKey(getTeamData().getTeamKey());
                            newMemberDataList.add(newMemberData);
                        }
                        teamMemberDataList.addAll(newMemberDataList);
                        teamMemberListAdapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    public void onItemClick(int position, View view, MemberEntity data) {

    }

    @Override
    public void onGetTeamMemberList(List<MemberEntity> memberData) {
        teamMemberDataList.addAll(memberData);
        teamMemberListAdapter.notifyDataSetChanged();
    }

    @Override
    public void failedGetTeamMemberList() {
        Toast.makeText(TeamMemberListActivity.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }
}