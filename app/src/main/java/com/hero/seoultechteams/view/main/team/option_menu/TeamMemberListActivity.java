package com.hero.seoultechteams.view.main.team.option_menu;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.option_menu.InviteActivity.EXTRA_INVITE_USER;
import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TEAM_MEMBER_LIST;
import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.contract.TeamMemberListContract;
import com.hero.seoultechteams.view.main.team.option_menu.presenter.TeamMemberListPresenter;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeamMemberListActivity extends BaseActivity implements View.OnClickListener, OnRecyclerItemClickListener<MemberEntity>, TeamMemberListContract.View {

    private ImageView btnBack;
    private CircleImageView ivMemberProfile;
    private MaterialButton btnGoToInvite;
    private RecyclerView rvTeamMemberList;
    private final ArrayList<MemberEntity> teamMemberDataList = new ArrayList<>();
    private TeamMemberListAdapter teamMemberListAdapter;
    private ActivityResultLauncher<Intent> photoResultLauncher;

    private final TeamMemberListContract.Presenter presenter = new TeamMemberListPresenter(this,
            Injector.getInstance().provideGetMemberListUseCase());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_list);
        initView();
        initTeamMemberListRecyclerViewAdapter();
        presenter.getMemberDataListFromDatabase(getTeamData(), teamMemberDataList);
        setOnClickListener();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        btnGoToInvite = findViewById(R.id.btn_go_to_invite);
        ivMemberProfile = findViewById(R.id.iv_member_profile);
        rvTeamMemberList = findViewById(R.id.rv_team_member_list);
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
        rvTeamMemberList.setAdapter(teamMemberListAdapter);
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
        btnGoToInvite.setOnClickListener(this);
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

    // TODO: 2022-09-27 멤버 프로필 이미지 클릭할 때 PhotoActivity로 이동하는 것을 어댑터에서 처리해야 하나요?
    private void intentMemberProfilePhoto(String profileImageUrl) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
        photoResultLauncher.launch(intent);
    }


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