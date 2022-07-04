package com.hero.seoultechteams.view.main.team;

import androidx.appcompat.widget.PopupMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.team.TeamRepository;
import com.hero.seoultechteams.database.team.entity.TeamData;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;


public class TeamDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, btnOptionMenu;
    private EditText editTeamName, editTeamDesc;
    public static final String EXTRA_UPDATE_TEAM = "updateTeam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        initView();
        setOnClickListener();
        addTextWatcher();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        btnOptionMenu = findViewById(R.id.iv_option_menu);
        editTeamName = findViewById(R.id.edit_team_name);
        editTeamDesc = findViewById(R.id.edit_team_desc);

        showUpdatedTeamDetail();
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(this);
        btnOptionMenu.setOnClickListener(this);
    }

    private TeamData getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    private void showUpdatedTeamDetail() {
        TeamData teamData = getTeamData();
        String myUserKey = getCurrentUser().getUid();
        if (myUserKey.equals(teamData.getLeaderKey())) {
            toggleEditText(editTeamName, true);
            toggleEditText(editTeamDesc, true);
        } else {
            toggleEditText(editTeamName, false);
            toggleEditText(editTeamDesc, false);
        }

        String teamName = teamData.getTeamName();
        String teamDesc = teamData.getTeamDesc();
        editTeamName.setText(teamName);
        editTeamDesc.setText(teamDesc);
        if (TextUtils.isEmpty(teamDesc)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editTeamDesc.requestFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(editTeamDesc, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        } else {
            editTeamName.clearFocus();
            editTeamDesc.clearFocus();
        }
    }

    private void toggleEditText(EditText editText, boolean enabled) {
        editText.setFocusable(enabled);
        editText.setFocusableInTouchMode(enabled);
        editText.setClickable(enabled);
    }

    // Team의 제목이 아무것도 안쓰여 있으면 뒤로가기가 활성화되지 않는다.
    private void addTextWatcher() {
        editTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivBack.setClickable(false);
                    Toast.makeText(TeamDetailActivity.this, "팀 이름을 입력해야 합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    ivBack.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                updateTeamDetail();
                break;
//            case R.id.iv_option_menu:
//                showTeamDetailOptionMenu();
//                break;
        }
    }

    private void updateTeamDetail() {
        TeamRepository teamRepository = new TeamRepository(this);
        final String teamName = editTeamName.getText().toString();
        final String teamDesc = editTeamDesc.getText().toString();

        final TeamData teamData = getTeamData();
        teamData.setTeamName(teamName);
        teamData.setTeamDesc(teamDesc);

        teamRepository.updateTeam(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_UPDATE_TEAM, teamData);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(TeamDetailActivity.this, "데이터가 수정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, teamData);
    }

//    private void showTeamDetailOptionMenu(){
//        PopupMenu popupMenu = new PopupMenu(this, btnOptionMenu);
//        popupMenu.getMenuInflater().inflate(R.menu.menu_team_detail_option, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_delete_team:
//                        openDeleteTeamDialog();
//                        break;
//                }
//                return true;
//            }
//        });
//        popupMenu.show();
//    }
//
//    private void openDeleteTeamDialog() {
//        String delete_todo_message = "팀을 삭제하시겠습니까?";
//        String positiveText = "예";
//        String negativeText = "아니오";
//        new MaterialAlertDialogBuilder(this).setMessage(delete_todo_message)
//                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create()
//                .show();
//    }
}