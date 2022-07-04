package com.hero.seoultechteams.view.main.team.option_menu;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.MemberRepository;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.option_menu.InviteActivity.EXTRA_INVITE_USER;
import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TEAM_MEMBER_LIST;
import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;


public class TeamMemberListActivity extends BaseActivity implements View.OnClickListener, OnRecyclerItemClickListener<MemberData> {

    private ImageView btnBack;
    private CircleImageView ivMemberProfile;
    private MaterialButton btnGoToInvite;
    private RecyclerView rvTeamMemberList;
    private ArrayList<MemberData> teamMemberDataList = new ArrayList<>();
    private TeamMemberListAdapter teamMemberListAdapter;
    public static final int INVITE_MEMBER_REQ_CODE = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_list);
        initView();
        initTeamMemberListRecyclerViewAdapter();
        getMemberDataListFromDatabase();
        setOnClickListener();

        Log.d("zxcv1", "onCreate: 초대 전 팀원들 목록의 크기 : (teamMemberDataList) " + teamMemberDataList.size());
        Log.d("zxcv2", "onCreate: 초대 전 팀원들 목록의 크기 : (getTeamMemberDataList()) " + getTeamMemberDataList().size());
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        btnGoToInvite = findViewById(R.id.btn_go_to_invite);
        ivMemberProfile = findViewById(R.id.iv_member_profile);
        rvTeamMemberList = findViewById(R.id.rv_team_member_list);
    }

    private void initTeamMemberListRecyclerViewAdapter() {
        teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberDataList, new TeamMemberListAdapter.OnMemberProfileImageClickListener() {
            @Override
            public void profileImageOnClick(String profileImageUrl) {
                Intent intent = new Intent(TeamMemberListActivity.this, PhotoActivity.class);
                intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
                startActivity(intent);
            }
        });
        teamMemberListAdapter.setLeaderKey(getTeamData().getLeaderKey());
        teamMemberListAdapter.setOnRecyclerItemClickListener(this);
        rvTeamMemberList.setAdapter(teamMemberListAdapter);
    }

    private void getMemberDataListFromDatabase() {
        MemberRepository memberRepository = new MemberRepository(this);
        String teamKey = getTeamData().getTeamKey();
        memberRepository.getMemberList(new OnCompleteListener<ArrayList<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
                if (isSuccess && data != null) {
                    teamMemberDataList.addAll(data);
                    teamMemberListAdapter.notifyDataSetChanged();
                    Log.d("zxcv3", "getMemberDataListFromDatabase(): 초대 전 팀원들 목록의 크기 : (teamMemberDataList) " + teamMemberDataList.size());
                    Log.d("zxcv4", "getMemberDataListFromDatabase(): 초대 전 팀원들 목록의 크기 : (getTeamMemberDataList()) " + getTeamMemberDataList().size());
                    Log.d("zxcv5", "---------------------------------------------------------------------------------------------- ");
                } else {
                    Toast.makeText(TeamMemberListActivity.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, teamKey);
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

    private TeamData getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    private ArrayList<MemberData> getTeamMemberDataList() {
        return getIntent().getParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST);
    }

    private void intentInviteActivity() {
        Intent intent = new Intent(this, InviteActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
        intent.putParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST, teamMemberDataList);
        startActivityForResult(intent, INVITE_MEMBER_REQ_CODE);
    }

    private void intentMemberProfilePhoto(String profileImageUrl) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INVITE_MEMBER_REQ_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<UserData> inviteUserDataList = data.getParcelableArrayListExtra(EXTRA_INVITE_USER);
                    ArrayList<MemberData> newMemberDataList = new ArrayList<>();
                    MemberData newMemberData = new MemberData();
                    for (UserData userData : inviteUserDataList) {
                        newMemberData.setKey(userData.getKey());
                        newMemberData.setName(userData.getName());
                        newMemberData.setEmail(userData.getEmail());
                        newMemberData.setProfileImageUrl(userData.getProfileImageUrl());
                        newMemberData.setTeamKey(getTeamData().getTeamKey());
                        newMemberDataList.add(newMemberData);
                    }
                    teamMemberDataList.addAll(newMemberDataList);
                    teamMemberListAdapter.notifyDataSetChanged();
                    Log.d("zxcv6", "onActivityResult: 초대 후 팀원들 목록의 크기 : (teamMemberDataList) " + teamMemberDataList.size());
                    Log.d("zxcv7", "onActivityResult: 초대 후 팀원들 목록의 크기 : (getTeamMemberDataList()) " + getTeamMemberDataList().size());
                    Log.d("zxcv8", "---------------------------------------------------------------------------------------------- ");
                }
                break;
        }
    }

    @Override
    public void onItemClick(int position, View view, MemberData data) {

    }
}