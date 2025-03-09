package com.hero.seoultechteams.view.main.team;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityCreateTeamBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.main.team.contract.CreateTeamContract;
import com.hero.seoultechteams.view.main.team.presenter.CreateTeamPresenter;

public class CreateTeamActivity extends BaseActivity<ActivityCreateTeamBinding> implements View.OnClickListener, CreateTeamContract.View {

    public static final String EXTRA_CREATE_TEAM = "createTeam";
    private final CreateTeamContract.Presenter presenter = new CreateTeamPresenter(this,
            Injector.getInstance().provideAddTeamUseCase(),
            Injector.getInstance().provideGetAccountProfileUseCase());

    @Override
    protected ActivityCreateTeamBinding getViewBinding() {
        return ActivityCreateTeamBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.btn_finish_create_team) {
            addTeamToDatabase();
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

        Toast.makeText(this, "팀을 생성했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedAddTeam() {
        Toast.makeText(CreateTeamActivity.this, "팀 생성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }
}