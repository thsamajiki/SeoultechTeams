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
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityTeamDetailBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.main.team.contract.TeamDetailContract;
import com.hero.seoultechteams.view.main.team.presenter.TeamDetailPresenter;

public class TeamDetailActivity extends BaseActivity implements View.OnClickListener, TeamDetailContract.View {
    private ActivityTeamDetailBinding binding;
    public static final String EXTRA_UPDATE_TEAM = "updateTeam";
    public static final String EXTRA_TEAM_KEY = "teamKey";
    private final TeamDetailContract.Presenter presenter = new TeamDetailPresenter(this,
            Injector.getInstance().provideUpdateTeamDetailUseCase(),
            Injector.getInstance().provideGetTeamUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initView();
        setOnClickListener();
        addTextWatcher();
    }

    private void initView() {
        String teamKey = getIntent().getStringExtra(EXTRA_TEAM_KEY);

        presenter.requestTeamData(teamKey);
    }

    private void initializeTeamDetail(TeamEntity teamEntity) {
        String myUserKey = getCurrentUser().getUid();
        if (myUserKey.equals(teamEntity.getLeaderKey())) {
            toggleEditText(binding.editTeamName, true);
            toggleEditText(binding.editTeamDesc, true);
        } else {
            toggleEditText(binding.editTeamName, false);
            toggleEditText(binding.editTeamDesc, false);
        }
        String teamName = teamEntity.getTeamName();
        String teamDesc = teamEntity.getTeamDesc();
        binding.editTeamName.setText(teamName);
        binding.editTeamDesc.setText(teamDesc);
        if (TextUtils.isEmpty(teamDesc)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.editTeamDesc.requestFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(binding.editTeamDesc, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        } else {
            binding.editTeamName.clearFocus();
            binding.editTeamDesc.clearFocus();
        }
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.ivOptionMenu.setOnClickListener(this);
    }

    private void toggleEditText(EditText editText, boolean enabled) {
        editText.setFocusable(enabled);
        editText.setFocusableInTouchMode(enabled);
        editText.setClickable(enabled);
    }

    // Team 의 제목이 아무것도 안쓰여 있으면 뒤로가기가 활성화되지 않는다.
    private void addTextWatcher() {
        binding.editTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.ivBack.setClickable(false);
                    Toast.makeText(TeamDetailActivity.this, "팀 이름을 입력해야 합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    binding.ivBack.setClickable(true);
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
        final String teamName = binding.editTeamName.getText().toString();
        final String teamDesc = binding.editTeamDesc.getText().toString();
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