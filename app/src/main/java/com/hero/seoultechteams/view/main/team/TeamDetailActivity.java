package com.hero.seoultechteams.view.main.team;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.main.team.contract.TeamDetailContract;
import com.hero.seoultechteams.view.main.team.presenter.TeamDetailPresenter;

public class TeamDetailActivity extends BaseActivity implements View.OnClickListener, TeamDetailContract.View {
    private ImageView ivBack, ivOptionMenu;
    private EditText editTeamName, editTeamDesc;
    public static final String EXTRA_UPDATE_TEAM = "updateTeam";
    public static final String EXTRA_TEAM_KEY = "teamKey";
    private final TeamDetailContract.Presenter presenter = new TeamDetailPresenter(this,
            Injector.getInstance().provideUpdateTeamDetailUseCase(),
            Injector.getInstance().provideGetTeamUseCase());

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
        ivOptionMenu = findViewById(R.id.iv_option_menu);
        editTeamName = findViewById(R.id.edit_team_name);
        editTeamDesc = findViewById(R.id.edit_team_desc);

        String teamKey = getIntent().getStringExtra(EXTRA_TEAM_KEY);

        presenter.requestTeamData(teamKey);
    }

    private void initializeTeamDetail(TeamEntity teamEntity) {
        String myUserKey = getCurrentUser().getUid();
        if (myUserKey.equals(teamEntity.getLeaderKey())) {
            toggleEditText(editTeamName, true);
            toggleEditText(editTeamDesc, true);
        } else {
            toggleEditText(editTeamName, false);
            toggleEditText(editTeamDesc, false);
        }
        String teamName = teamEntity.getTeamName();
        String teamDesc = teamEntity.getTeamDesc();
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

    private void setOnClickListener() {
        ivBack.setOnClickListener(this);
        ivOptionMenu.setOnClickListener(this);
    }

    private void toggleEditText(EditText editText, boolean enabled) {
        editText.setFocusable(enabled);
        editText.setFocusableInTouchMode(enabled);
        editText.setClickable(enabled);
    }

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
        }
    }

    private void updateTeamDetail() {
        final String teamName = editTeamName.getText().toString();
        final String teamDesc = editTeamDesc.getText().toString();
        presenter.updateTeamDetail(teamName, teamDesc);
    }

    @Override
    public void updatedTeamDetail(TeamEntity data) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_UPDATE_TEAM, data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedUpdateTeamDetail() {
        Toast.makeText(TeamDetailActivity.this, "데이터가 수정되지 않았습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadTeam(TeamEntity data) {
        initializeTeamDetail(data);
    }

    @Override
    public void failedLoadTeam() {
        Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
    }
}