package com.hero.seoultechteams.view.main.team.option_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.UserRepository;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;

import java.util.ArrayList;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TEAM_MEMBER_LIST;


public class InviteActivity extends AppCompatActivity implements View.OnClickListener, OnRecyclerItemClickListener<UserData> {

    private ImageView ivBack;
    private EditText inputEmailOrUser;
    private CheckBox chkboxInvite;
    private TextView btnInvite;
    private ArrayList<MemberData> currentTeamMemberDataList = new ArrayList<>();   // 초대하기 전 팀원 목록
    private ArrayList<UserData> searchedUserDataList = new ArrayList<>();   // 검색된 사용자 목록
    private ArrayList<UserData> inviteUserDataList = new ArrayList<>();     // 초대할 팀원 목록
    private RecyclerView rvInviteUserList;
    private InviteAdapter inviteAdapter;
    private UserRepository userRepository = new UserRepository(this);
    public static final String EXTRA_INVITE_USER = "addInvitedUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        initView();
        initInviteUserListRecyclerViewAdapter();
        setEditorActionListener();
        setOnClickListener();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        inputEmailOrUser = findViewById(R.id.input_email_or_user);
        chkboxInvite = findViewById(R.id.chkbox_invite);
        btnInvite = findViewById(R.id.btn_invite);
        rvInviteUserList = findViewById(R.id.rv_invite_user_list);
    }

    private void initInviteUserListRecyclerViewAdapter() {
        inviteAdapter = new InviteAdapter(this, searchedUserDataList, new InviteAdapter.OnInviteMemberItemCheckListener() {
            @Override
            public void inviteMemberOnCheck(UserData data) {
                inviteUserDataList.add(data);
                Log.d("qwer1", "InviteActivity-initInviteUserListRecyclerViewAdapter()-체크된 사용자 목록의 크기 : " + inviteUserDataList.size());
                for (UserData userData : inviteUserDataList) {
                    Log.d("qwer2", "InviteActivity-initInviteUserListRecyclerViewAdapter()-체크된 사용자의 이름 : " + userData.getName());
                }
            }
        });
        inviteAdapter.setOnRecyclerItemClickListener(this);
        rvInviteUserList.setAdapter(inviteAdapter);
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(this);
        btnInvite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_invite:
                openInviteUserDialog();
                break;
        }
    }

    private void setEditorActionListener() {
        inputEmailOrUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        String input = v.getText().toString();
                        if (checkEmailValid(input)) {
                            searchUserByEmail(input);
                        } else {
                            searchUserByName(input);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void searchUserByName(String userName) {
        userRepository.getSearchedUserListByUserName(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                if (isSuccess && data != null) {
                    searchedUserDataList.clear();
                    addUserList(data);
                } else {
                    Toast.makeText(InviteActivity.this, "사용자 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
                inviteAdapter.notifyDataSetChanged();
            }
        }, userName);
    }

    private void searchUserByEmail(String userEmail) {
        userRepository.getSearchedUserListByUserEmail(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                searchedUserDataList.clear();
                if (isSuccess && data != null) {
                    addUserList(data);
                } else {
                    Toast.makeText(InviteActivity.this, "사용자 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
                inviteAdapter.notifyDataSetChanged();
            }
        }, userEmail);
    }

    private void addUserList(ArrayList<UserData> data) {
        for (UserData userData : data) {
            if (isNotMember(userData)) {
                searchedUserDataList.add(userData);
            }
        }
    }

    private boolean isNotMember(UserData data) {
        return !isMember(data);
    }

    private boolean isMember(UserData data) {
        for (MemberData memberData : getTeamMemberDataList()) {
            if (data.getKey().equals(memberData.getKey())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void openInviteUserDialog() {
        String askIfInvite = "초대하시겠습니까?";
        String yesInvite = "예";
        String noInvite = "아니오";
        new MaterialAlertDialogBuilder(this).setMessage(askIfInvite)
                .setPositiveButton(yesInvite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("qwer5", "InviteActivity-openInviteUserDialog() - " + inviteUserDataList.size() + "명을 초대하시겠습니까?: Yes");
                        for (UserData userData : inviteUserDataList) {
                            Log.d("qwer6", "InviteActivity-openInviteUserDialog() - " + userData.getName() + "을 초대하시겠습니까?: Yes");
                        }
                        addInviteInfoToDatabase();
                    }
                })
                .setNegativeButton(noInvite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void addInviteInfoToDatabase() {
//        MemberRepository memberRepository = new MemberRepository(this);
//        memberRepository.addMemberToTeam(new OnCompleteListener<ArrayList<MemberData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
//                if (isSuccess && data != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra(EXTRA_INVITE_MEMBER, data);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } else {
//                    Toast.makeText(InviteActivity.this, "팀원을 초대하는 데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, getTeamData(), inviteUserDataList, getMemberDataList());

        UserRepository userRepository = new UserRepository(this);
        userRepository.addInvitedUser(new OnCompleteListener<ArrayList<UserData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<UserData> data) {
                if (isSuccess && data != null) {
                    Log.d("qwer3", "InviteActivity-addInviteInfoToDatabase()-onComplete: 초대하기 전 팀원들 수 : " + getTeamMemberDataList().size());
                    for (MemberData memberData : getTeamMemberDataList()) {
                        Log.d("qwer4", "InviteActivity-addInviteInfoToDatabase()-onComplete: 초대하기 전 팀원의 이름 : " + memberData.getName());
                    }
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(EXTRA_INVITE_USER, data);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Log.d("qwer9", "addInviteInfoToDatabase()-onComplete: 초대하기 전 팀원들 수 : " + getTeamMemberDataList().size());
                    Toast.makeText(InviteActivity.this, "팀원을 초대하는 데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, getTeamData(), inviteUserDataList, getTeamMemberDataList());
    }

//    private ArrayList<MemberData> getTeamMemberListFromDatabase() {
//        MemberRepository memberRepository = new MemberRepository(this);
//        memberRepository.getMemberList(new OnCompleteListener<ArrayList<MemberData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
//                if (isSuccess && data != null) {
//                    currentTeamMemberDataList = data;
//                    Log.d("abcd11", "InviteActivity-getTeamMemberListFromDatabase()-onComplete: 초대하기 전 팀원들 수 : " + currentTeamMemberDataList.size());
//                    for (MemberData memberData : currentTeamMemberDataList) {
//                        Log.d("abcd12", "InviteActivity-getTeamMemberListFromDatabase()-onComplete: 초대하기 전 팀원의 이름 : " + memberData.getName());
//                    }
//                } else {
//                    Toast.makeText(InviteActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, getTeamData().getTeamKey());
//
//        return currentTeamMemberDataList;
//    }

    private ArrayList<MemberData> getTeamMemberDataList() {
        return getIntent().getParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST);
    }

    private TeamData getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }


    @Override
    public void onItemClick(int position, View view, UserData data) {

    }
}