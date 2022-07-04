package com.hero.seoultechteams.view.main.team.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.member.MemberRepository;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.TodoRepository;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.TeamParticipationActivity;
import com.hero.seoultechteams.view.main.team.option_menu.TeamMemberListActivity;

import java.util.ArrayList;

import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.todo.AddTodoActivity.EXTRA_ADD_TODO;
import static com.hero.seoultechteams.view.main.team.todo.TodoDetailActivity.EXTRA_UPDATE_TODO;

public class TeamTodoListActivity extends BaseActivity implements View.OnClickListener, OnRecyclerItemClickListener<TodoData>, SwipeRefreshLayout.OnRefreshListener {

    private ImageView btnBack, btnTeamOptionMenu, btnTodoOptionMenu;
    private MaterialCardView mcvCreateTodo;
    private RecyclerView rvTeamTodoList;
    private SwipeRefreshLayout srlTeamTodoList;
    private ArrayList<TodoData> teamTodoDataList = new ArrayList<>();
    private ArrayList<MemberData> teamMemberDataList;
    private TeamTodoListAdapter teamTodoListAdapter;

    public static final int ADD_TODO_REQ_CODE = 333;   // Todo를 생성하는 요청 코드
    public static final int UPDATE_TODO_REQ_CODE = 888; // Todo를 업데이트하는 요청 코드
    public static final int UPDATE_MEMBER_REQ_CODE = 999;   // Todo를 담당한 멤버에 대한 성과를 업데이트하는 요청 코드
    public static final String EXTRA_TODO_DATA = "todoData";
    public static final String EXTRA_TEAM_MEMBER_LIST = "teamMemberDataList";
    private MemberRepository memberRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_todo_list);
        initView();
        initTeamTodoListAdapter();
        getTeamTodoDataListFromDatabase();
        getTeamMemberListFromDatabase();
        setOnClickListener();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        btnTeamOptionMenu = findViewById(R.id.iv_team_option_menu);
        mcvCreateTodo = findViewById(R.id.mcv_create_todo);
        btnTodoOptionMenu = findViewById(R.id.btn_todo_option_menu);
        rvTeamTodoList = findViewById(R.id.rv_team_todo_list);
        srlTeamTodoList = findViewById(R.id.srl_team_todo_list);
        srlTeamTodoList.setOnRefreshListener(this);
    }

    private void initTeamTodoListAdapter() {
        String myKey = getCurrentUser().getUid();
        teamTodoListAdapter = new TeamTodoListAdapter(this, teamTodoDataList, new TeamTodoListAdapter.OnBtnStateTodoClickListener() {
            @Override
            public void btnStateTodoOnClick(TodoData data) {
                // 해당 Todo를 담당한 멤버의 MemberData의 평가 변수가 갱신되어야 하고,
                // 해당 Todo를 담당한 멤버의 MemberData를 인텐트를 TeamParticipationActivity로 보내준다.
            }
        });
        teamTodoListAdapter.setLeader(getTeamData().getLeaderKey().equals(myKey));
        teamTodoListAdapter.setOnRecyclerItemClickListener(this);
        rvTeamTodoList.setAdapter(teamTodoListAdapter);
    }

    private void getTeamTodoDataListFromDatabase() {
        TodoRepository todoRepository = new TodoRepository(this);
        String teamKey = getTeamData().getTeamKey();
        todoRepository.getTeamTodoList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                if (isSuccess) {
                    if (data != null) {
                        teamTodoDataList.clear();
                        teamTodoDataList.addAll(data);
                        teamTodoListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TeamTodoListActivity.this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TeamTodoListActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

                if (srlTeamTodoList.isRefreshing()) {
                    srlTeamTodoList.setRefreshing(false);
                }
            }
        }, teamKey);
    }

    private TeamData getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
        mcvCreateTodo.setOnClickListener(this);
        btnTeamOptionMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_team_option_menu:
                showTeamTodoListOptionMenu();
                break;
            case R.id.mcv_create_todo:
                intentAddTodo();
                break;
        }
    }

    private void showTeamTodoListOptionMenu(){
        PopupMenu popupMenu = new PopupMenu(this, btnTeamOptionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_team_todolist_actionbar_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.menu_team_member_list:
                        intent = new Intent(TeamTodoListActivity.this, TeamMemberListActivity.class);
                        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
                        intent.putParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST, teamMemberDataList);
                        startActivity(intent);
                        break;
                    case R.id.menu_team_participation:
                        intent = new Intent(TeamTodoListActivity.this, TeamParticipationActivity.class);
                        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
                        intent.putParcelableArrayListExtra(EXTRA_TEAM_MEMBER_LIST, teamMemberDataList);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void getTeamMemberListFromDatabase() {
        if (memberRepository == null) {
            memberRepository = new MemberRepository(this);
        }
        memberRepository.getMemberList(new OnCompleteListener<ArrayList<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<MemberData> data) {
                if (isSuccess && data != null) {
                    teamMemberDataList = data;
                } else {
                    Toast.makeText(TeamTodoListActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, getTeamData().getTeamKey());
    }

    private void intentAddTodo() {
        Intent intent = new Intent(this, AddTodoActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
        intent.putExtra(EXTRA_TEAM_MEMBER_LIST, teamMemberDataList);
        startActivityForResult(intent, ADD_TODO_REQ_CODE);
    }

    @Override
    public void onItemClick(int position, View view, TodoData data) {
        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra(EXTRA_TODO_DATA, data);
        startActivityForResult(intent, UPDATE_TODO_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ADD_TODO_REQ_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    TodoData todoData = data.getParcelableExtra(EXTRA_ADD_TODO);
                    teamTodoDataList.add(0, todoData);
                    teamTodoListAdapter.notifyDataSetChanged();
                }
                break;
            case UPDATE_TODO_REQ_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    TodoData todoData = data.getParcelableExtra(EXTRA_UPDATE_TODO);
                    int index = teamTodoDataList.indexOf(todoData);
                    if (index != -1) {
                        teamTodoDataList.set(index, todoData);
                        teamTodoListAdapter.notifyItemChanged(index);
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        memberRepository.setRefresh(true);
        getTeamTodoDataListFromDatabase();
        getTeamMemberListFromDatabase();
    }
}