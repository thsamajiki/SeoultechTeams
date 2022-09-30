package com.hero.seoultechteams.view.main.team.option_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.contract.InviteContract;
import com.hero.seoultechteams.view.main.team.option_menu.presenter.InvitePresenter;

import java.util.ArrayList;
import java.util.List;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TEAM_MEMBER_LIST;


public class InviteActivity extends AppCompatActivity implements View.OnClickListener, OnRecyclerItemClickListener<UserEntity>, InviteContract.View {

    private ImageView ivBack;
    private EditText inputEmailOrUser;
    private CheckBox chkBoxInvite;
    private TextView btnInvite;
    private RecyclerView rvInviteUserList;
    private InviteAdapter inviteAdapter;
    public static final String EXTRA_INVITE_USER = "addInvitedUser";
    private final InviteContract.Presenter presenter = new InvitePresenter(this,
            Injector.getInstance().provideAddNewMemberListUseCase(),
            Injector.getInstance().provideGetUserListByNameUseCase(),
            Injector.getInstance().provideGetUserListByEmailUseCase(),
            getTeamMemberDataList());

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
        chkBoxInvite = findViewById(R.id.chkbox_invite);
        btnInvite = findViewById(R.id.btn_invite);
        rvInviteUserList = findViewById(R.id.rv_invite_user_list);
    }

    private void initInviteUserListRecyclerViewAdapter() {
        inviteAdapter = new InviteAdapter(this);
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
                        presenter.getUserList(input);
                        break;
                }
                return true;
            }
        });
    }

    private void replaceUserList(List<UserEntity> data) {
        inviteAdapter.replaceAll(data);
    }

    private void openInviteUserDialog() {
        String askIfInvite = "초대하시겠습니까?";
        String yesInvite = "예";
        String noInvite = "아니오";
        new MaterialAlertDialogBuilder(this).setMessage(askIfInvite)
                .setPositiveButton(yesInvite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.inviteUserList(getTeamData(), inviteAdapter.getInviteUserDataList(), getTeamMemberDataList());
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

    private List<MemberEntity> getTeamMemberDataList() {
        return getIntent().getParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST);
    }

    private TeamEntity getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }


    @Override
    public void onItemClick(int position, View view, UserEntity data) {
        switch (view.getId()) {
            case R.id.iv_member_profile:
                break;
        }
    }

    @Override
    public void onAddNewMemberList(TeamEntity teamData, ArrayList<UserEntity> inviteUserDataList, List<MemberEntity> teamMemberDataList) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_INVITE_USER, inviteUserDataList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedAddNewMemberList() {
        Toast.makeText(InviteActivity.this, "팀원을 초대하는 데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetUserListByName(List<UserEntity> data) {
        replaceUserList(data);
    }

    @Override
    public void failedGetUserListByName() {
        Toast.makeText(InviteActivity.this, "사용자 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetUserListByEmail(List<UserEntity> data) {
        replaceUserList(data);
    }

    @Override
    public void failedGetUserListByEmail() {
        Toast.makeText(InviteActivity.this, "사용자 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }
}