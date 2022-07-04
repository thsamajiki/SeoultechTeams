package com.hero.seoultechteams.view.main.team;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.team.TeamRepository;
import com.hero.seoultechteams.database.team.entity.TeamData;


public class CreateTeamActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack;
    private MaterialButton btnFinishCreateTeam;
    private EditText editCreateTeamName, editCreateTeamDesc;
    public static final String EXTRA_CREATE_TEAM = "createTeam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        initView();
        setOnClickListener();
        addTextWatcher();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        btnFinishCreateTeam = findViewById(R.id.btn_finish_create_team);
        editCreateTeamName = findViewById(R.id.edit_create_team_name);
        editCreateTeamDesc = findViewById(R.id.edit_create_team_desc);
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
        btnFinishCreateTeam.setOnClickListener(this);
    }

    private void addTextWatcher() {
        editCreateTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    btnFinishCreateTeam.setEnabled(true);
                } else {
                    btnFinishCreateTeam.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_finish_create_team:
                addTeamToDatabase();
                break;
        }
    }

    private void addTeamToDatabase() {
        TeamRepository teamRepository = new TeamRepository(this);
        teamRepository.addTeam(new OnCompleteListener<TeamData>() {
            @Override
            public void onComplete(boolean isSuccess, TeamData data) {
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_CREATE_TEAM, data);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(CreateTeamActivity.this, "팀 생성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, createTeamData());
    }

    private TeamData createTeamData() {
        TeamData teamData = new TeamData();
        teamData.setTeamName(editCreateTeamName.getText().toString());
        teamData.setTeamDesc(editCreateTeamDesc.getText().toString());
        teamData.setCreatedDate(System.currentTimeMillis());

        return teamData;
    }
}