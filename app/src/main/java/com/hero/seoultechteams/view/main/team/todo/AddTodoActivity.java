package com.hero.seoultechteams.view.main.team.todo;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityAddTodoBinding;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.TeamMemberListAdapter;
import com.hero.seoultechteams.view.main.team.todo.contract.AddTodoContract;
import com.hero.seoultechteams.view.main.team.todo.presenter.AddTodoPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTodoActivity extends BaseActivity<ActivityAddTodoBinding> implements View.OnClickListener, TextWatcher, OnRecyclerItemClickListener<MemberEntity>, AddTodoContract.View {

    private TeamMemberListAdapter teamMemberListAdapter;
    private final ArrayList<MemberEntity> teamMemberDataList = new ArrayList<>();
    public static final String EXTRA_ADD_TODO = "addTodo";
    private MemberEntity managerData;
    private final AddTodoContract.Presenter presenter = new AddTodoPresenter(this,
            Injector.getInstance().provideAddTodoUseCase(),
            Injector.getInstance().provideGetMemberListUseCase());

    @Override
    protected ActivityAddTodoBinding getViewBinding() {
        return ActivityAddTodoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        setOnClickListeners();
        addTextWatcher();
        initTeamMemberListAdapter();
        getTeamMemberListFromDatabase();
    }

    private void initView() {
        binding.editAddTodoTitle.requestFocus();
    }

    private void setOnClickListeners() {
        binding.viewAddTodoBackground.setOnClickListener(this);
        binding.llSetManager.setOnClickListener(this);
        binding.btnFinishAddTodo.setOnClickListener(this);
    }

    private void addTextWatcher() {
        binding.editAddTodoTitle.addTextChangedListener(this);
    }

    private void initTeamMemberListAdapter() {
        teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberDataList);
        teamMemberListAdapter.memberCallBack(new TeamMemberListAdapter.OnMemberProfileImageClickListener() {
            @Override
            public void profileImageOnClick(String profileImageUrl) {
            }
        });
        teamMemberListAdapter.setOnRecyclerItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_add_todo_background) {
            finish();
        } else if (view.getId() == R.id.btn_finish_add_todo) {
            openDatePicker();
        } else if (view.getId() == R.id.ll_set_manager) {
            openMemberListDialog();
        }
    }

    private void openMemberListDialog() {
        String memberListTitle = "담당자를 선택해주세요";
        View view = getLayoutInflater().inflate(R.layout.dialog_member_list, null);

        MaterialAlertDialogBuilder memberListDialog = new MaterialAlertDialogBuilder(this)
                .setTitle(memberListTitle)
                .setView(view);

        final AlertDialog dialog = memberListDialog.create();
        RecyclerView rvTeamMemberList = view.findViewById(R.id.rv_team_member_list);
        teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberDataList);
        teamMemberListAdapter.setLeaderKey(getTeamData().getLeaderKey());
        teamMemberListAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<MemberEntity>() {
            @Override
            public void onItemClick(int position, View view, MemberEntity data) {
                managerData = data;
                setManagerProfile(data);

                dialog.dismiss();
            }
        });
        rvTeamMemberList.setAdapter(teamMemberListAdapter);

        dialog.show();
    }

    private void setManagerProfile(MemberEntity memberData) {
        if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
            Glide.with(this).load(R.drawable.sample_profile_image).into(binding.ivManagerProfile);
        } else {
            Glide.with(AddTodoActivity.this).load(memberData.getProfileImageUrl()).into(binding.ivManagerProfile);
        }
    }

    private void getTeamMemberListFromDatabase() {
        presenter.getTeamMemberList(getTeamData().getTeamKey());
    }

    private MemberEntity getMyDataFromMemberList(List<MemberEntity> memberDataList) {
        for (MemberEntity memberData : memberDataList) {
            if (memberData.getKey().equals(getCurrentUser().getUid())) {
                return memberData;
            }
        }
        return null;
    }


    private TeamEntity getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }


    private void openDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                openTimePicker(year, month, dayOfMonth);
            }

        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void openTimePicker(final int year, final int month, final int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                long todoEndDateTime = calendar.getTimeInMillis();

                presenter.addTodoToDatabase(binding.editAddTodoTitle.getText().toString(), todoEndDateTime, managerData, getTeamData());
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    @Override
    public void addedTodoList(TodoEntity data) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADD_TODO, data);
        setResult(RESULT_OK, intent);
        finish();
        Toast.makeText(AddTodoActivity.this, "할 일을 생성했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedAddTodo() {
        Toast.makeText(AddTodoActivity.this, "할 일 생성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTeamMemberList(List<MemberEntity> data) {
        managerData = getMyDataFromMemberList(data);
        if (managerData != null) {
            setManagerProfile(managerData);
        }
        teamMemberDataList.addAll(data);
    }

    @Override
    public void failedTeamMemberList() {
        Toast.makeText(AddTodoActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        binding.btnFinishAddTodo.setEnabled(s.length() > 0);
    }

    @Override
    public void onItemClick(int position, View view, MemberEntity data) {

    }
}