package com.hero.seoultechteams.view.main.team.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.MemberRepository;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.TodoRepository;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.TeamMemberListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;

public class AddTodoActivity extends BaseActivity implements View.OnClickListener, TextWatcher, OnRecyclerItemClickListener<MemberData> {

    private MaterialButton btnFinishAddTodo;
    private EditText editAddTodoTitle;
    private CircleImageView ivManagerProfile;
    private View viewAddTodoBackground;
    private LinearLayout llSetManager;
    private RecyclerView rvTeamMemberList;
    private TeamMemberListAdapter teamMemberListAdapter;
    private ArrayList<MemberData> teamMemberDataList = new ArrayList<>();
    public static final String EXTRA_ADD_TODO = "addTodo";
    public static final String EXTRA_MEMBER_DATA = "memberData";
    private MemberData managerData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        initView();
        setOnClickListener();
        addTextWatcher();
        initTeamMemberListAdapter();
        getTeamMemberListFromDatabase();
    }

    private void initView() {
        btnFinishAddTodo = findViewById(R.id.btn_finish_add_todo);
        editAddTodoTitle = findViewById(R.id.edit_add_todo_title);
        editAddTodoTitle.requestFocus();

        llSetManager = findViewById(R.id.ll_set_manager);
        rvTeamMemberList = findViewById(R.id.rv_team_member_list);
        ivManagerProfile = findViewById(R.id.iv_manager_profile);
        viewAddTodoBackground = findViewById(R.id.view_add_todo_background);
    }

    private void setOnClickListener() {
        viewAddTodoBackground.setOnClickListener(this);
        llSetManager.setOnClickListener(this);
        btnFinishAddTodo.setOnClickListener(this);
    }

    private void addTextWatcher() {
        editAddTodoTitle.addTextChangedListener(this);
    }

    private void initTeamMemberListAdapter() {
        teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberDataList, new TeamMemberListAdapter.OnMemberProfileImageClickListener() {
            @Override
            public void profileImageOnClick(String profileImageUrl) {

            }
        });
        teamMemberListAdapter.setOnRecyclerItemClickListener(this);
        //rvTeamMemberList.setAdapter(teamMemberListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_add_todo_background:
                finish();
                break;
            case R.id.btn_finish_add_todo:
                openDatePicker();
                break;
            case R.id.ll_set_manager:
                openMemberListDialog();
                break;
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
        teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberDataList, new TeamMemberListAdapter.OnMemberProfileImageClickListener() {
            @Override
            public void profileImageOnClick(String profileImageUrl) {

            }
        });
        teamMemberListAdapter.setLeaderKey(getTeamData().getLeaderKey());
        teamMemberListAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<MemberData>() {
            @Override
            public void onItemClick(int position, View view, MemberData data) {
                managerData = data;
                setManagerProfile(data);

                dialog.dismiss();
            }
        });
        rvTeamMemberList.setAdapter(teamMemberListAdapter);

        dialog.show();
    }

    private void setManagerProfile(MemberData memberData) {
        if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
            Glide.with(this).load(R.drawable.sample_profile_image).into(ivManagerProfile);
        } else {
            Glide.with(AddTodoActivity.this).load(memberData.getProfileImageUrl()).into(ivManagerProfile);
        }
    }

    private void getTeamMemberListFromDatabase() {
        MemberRepository memberRepository = new MemberRepository(this);
        memberRepository.getMemberList(new OnCompleteListener<ArrayList<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
                if (isSuccess && data != null) {
                    managerData = getMyDataFromMemberList(data);
                    if (managerData != null) {
                        setManagerProfile(managerData);
                    }
                    teamMemberDataList.addAll(data);
                } else {
                    Toast.makeText(AddTodoActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, getTeamData().getTeamKey());
    }

    private MemberData getMyDataFromMemberList(ArrayList<MemberData> memberDataList) {
        for (MemberData memberData : memberDataList) {
            if (memberData.getKey().equals(getCurrentUser().getUid())) {
                return memberData;
            }
        }
        return null;
    }


    private TeamData getTeamData() {
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
                addTodoToDatabase(createTodoData(todoEndDateTime));
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void addTodoToDatabase(TodoData todoData) {
        TodoRepository todoRepository = new TodoRepository(this);
        todoRepository.addTodo(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_ADD_TODO, data);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(AddTodoActivity.this, "할 일 생성에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, todoData);
    }

    private TodoData createTodoData(long todoEndDateTime) {
        TodoData todoData = new TodoData();
        todoData.setTodoTitle(editAddTodoTitle.getText().toString());
        todoData.setTeamKey(getTeamData().getTeamKey());
        todoData.setTeamName(getTeamData().getTeamName());
        todoData.setManagerEmail(managerData.getEmail());
        if (managerData.getProfileImageUrl() != null) {
            todoData.setManagerProfileImageUrl(managerData.getProfileImageUrl());
        }
        todoData.setManagerName(managerData.getName());
        todoData.setTodoCreatedTime(System.currentTimeMillis());
        todoData.setTodoState(TodoData.TODO_STATE_IN_PROGRESS);
        todoData.setTodoEndTime(todoEndDateTime);
        todoData.setUserKey(managerData.getKey());

        ArrayList<Event> eventHistory = new ArrayList<>();
        Event event = new Event();
        event.setEvent(Event.EVENT_CREATE);
        event.setTime(System.currentTimeMillis());
        eventHistory.add(event);
        todoData.setEventHistory(eventHistory);

        return todoData;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            btnFinishAddTodo.setEnabled(true);
        } else {
            btnFinishAddTodo.setEnabled(false);
        }
    }

    @Override
    public void onItemClick(int position, View view, MemberData data) {

    }
}