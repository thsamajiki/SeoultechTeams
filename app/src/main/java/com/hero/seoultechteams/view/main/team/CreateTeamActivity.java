package com.hero.seoultechteams.view.main.team;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityCreateTeamBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.main.team.contract.CreateTeamContract;
import com.hero.seoultechteams.view.main.team.presenter.CreateTeamPresenter;

public class CreateTeamActivity extends AppCompatActivity implements View.OnClickListener, CreateTeamContract.View {

    private ActivityCreateTeamBinding binding;
    public static final String EXTRA_CREATE_TEAM = "createTeam";
    private final CreateTeamContract.Presenter presenter = new CreateTeamPresenter(this,
            Injector.getInstance().provideAddTeamUseCase(),
            Injector.getInstance().provideGetAccountProfileUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTeamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setOnClickListener();
        addTextWatcher();
    }


    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.btnFinishCreateTeam.setOnClickListener(this);
    }

    private void addTextWatcher() {
        binding.editCreateTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    binding.btnFinishCreateTeam.setEnabled(true);
                } else {
                    binding.btnFinishCreateTeam.setEnabled(false);
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
        presenter.addTeamToDatabase(
                binding.editCreateTeamName.getText().toString(),
                binding.editCreateTeamDesc.getText().toString()
        );
    }

    @Override
    public void addedTeamList(TeamEntity data) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CREATE_TEAM, data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failedAddTeam() {
        Toast.makeText(CreateTeamActivity.this, "팀 생성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }
}