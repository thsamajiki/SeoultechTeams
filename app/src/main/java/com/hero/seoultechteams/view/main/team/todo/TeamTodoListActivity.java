package com.hero.seoultechteams.view.main.team.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.team.entity.TeamData;

import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.TeamParticipationActivity;
import com.hero.seoultechteams.view.main.team.option_menu.TeamMemberListActivity;
import com.hero.seoultechteams.view.main.team.todo.contract.TeamTodoListContract;
import com.hero.seoultechteams.view.main.team.todo.presenter.TeamTodoListPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_CONFIRMED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_DISMISSED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_IN_PROGRESS;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_SUBMITTED;
import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.todo.AddTodoActivity.EXTRA_ADD_TODO;
import static com.hero.seoultechteams.view.main.team.todo.TodoDetailActivity.EXTRA_UPDATE_TODO;

public class TeamTodoListActivity extends BaseActivity implements View.OnClickListener, OnRecyclerItemClickListener<TodoEntity>, SwipeRefreshLayout.OnRefreshListener, TeamTodoListContract.View {

    private ImageView btnBack, btnTeamOptionMenu, btnTodoOptionMenu;
    private MaterialCardView mcvCreateTodo;
    private RecyclerView rvTeamTodoList;
    private SwipeRefreshLayout srlTeamTodoList;
    private List<TodoEntity> teamTodoDataList = new ArrayList<>();
    private TeamTodoListAdapter teamTodoListAdapter;

    public static final int ADD_TODO_REQ_CODE = 333;   // Todo를 생성하는 요청 코드
    public static final int UPDATE_TODO_REQ_CODE = 888; // Todo를 업데이트하는 요청 코드
    public static final int UPDATE_MEMBER_REQ_CODE = 999;   // Todo를 담당한 멤버에 대한 성과를 업데이트하는 요청 코드
    public static final String EXTRA_TODO_DATA = "todoData";
    public static final String EXTRA_TEAM_MEMBER_LIST = "teamMemberDataList";

    private final ActivityResultLauncher<Intent> addTodoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        TodoEntity todoEntity = data.getParcelableExtra(EXTRA_ADD_TODO);
                        teamTodoDataList.add(0, todoEntity);
                        teamTodoListAdapter.notifyDataSetChanged();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> updateTodoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        TodoEntity todoEntity = data.getParcelableExtra(EXTRA_UPDATE_TODO);
                        int index = teamTodoDataList.indexOf(todoEntity);
                        if (index != -1) {
                            teamTodoDataList.set(index, todoEntity);
                            teamTodoListAdapter.notifyItemChanged(index);
                        }
                    }
                }
            }
    );

    private final TeamTodoListContract.Presenter presenter = new TeamTodoListPresenter(this,
            Injector.getInstance().provideGetTeamTodoListUseCase(),
            Injector.getInstance().provideUpdateTodoStateUseCase(),
            Injector.getInstance().provideSetRefreshUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_todo_list);
        initView();
        initTeamTodoListAdapter();
        presenter.getTeamTodoDataListFromDatabase(getTeamData().getTeamKey());
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
        teamTodoListAdapter = new TeamTodoListAdapter(this, teamTodoDataList);
        teamTodoListAdapter.todoCallBack(new TeamTodoListAdapter.OnBtnStateTodoClickListener() {
            @Override
            public void btnStateTodoOnClick(TodoEntity data) {

            }
        });
        teamTodoListAdapter.setLeader(getTeamData().getLeaderKey().equals(myKey));
        teamTodoListAdapter.setOnRecyclerItemClickListener(this);
        rvTeamTodoList.setAdapter(teamTodoListAdapter);
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
                        intent.putExtra(EXTRA_TEAM_DATA, getTeamData().toEntity());
                        startActivity(intent);
                        break;
                    case R.id.menu_team_participation:
                        intent = new Intent(TeamTodoListActivity.this, TeamParticipationActivity.class);
                        intent.putExtra(EXTRA_TEAM_DATA, getTeamData().toEntity());
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void intentAddTodo() {
        Intent intent = new Intent(this, AddTodoActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, getTeamData().toEntity());
        addTodoResultLauncher.launch(intent);
    }

    @Override
    public void onItemClick(int position, View view, TodoEntity data) {
        switch (view.getId()) {
            case R.id.btn_dismiss_todo:
                updateTodoState(true, position, data);
                break;
            case R.id.btn_state_todo:
                updateTodoState(false, position, data);
                break;
            default:
                Intent intent = new Intent(this, TodoDetailActivity.class);
                intent.putExtra(EXTRA_TODO_DATA, data);
                updateTodoResultLauncher.launch(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        presenter.setRefresh(true);

        teamTodoDataList.clear();
        presenter.getTeamTodoDataListFromDatabase(getTeamData().getTeamKey());
    }

    private void updateTodoState(boolean isDismissed, int position, TodoEntity data) {
        Event newEvent = new Event();
        switch (data.getTodoState()) {
            case TODO_STATE_IN_PROGRESS:
            case TODO_STATE_DISMISSED:
                data.setTodoState(TODO_STATE_SUBMITTED);
                newEvent.setEvent(Event.EVENT_SUBMIT);
                break;
            case TODO_STATE_SUBMITTED:
                if (isDismissed) {
                    data.setTodoState(TODO_STATE_DISMISSED);
                    newEvent.setEvent(Event.EVENT_DISMISS);
                } else {
                    data.setTodoState(TODO_STATE_CONFIRMED);
                    newEvent.setEvent(Event.EVENT_CONFIRM);
                }
                break;

        }

        newEvent.setTime(System.currentTimeMillis());
        data.getEventHistory().add(newEvent);

        presenter.updateTodoState(data, position);
    }

    @Override
    public void onGetTeamTodoList(List<TodoEntity> data) {
        teamTodoDataList.clear();
        teamTodoDataList.addAll(data);
        teamTodoListAdapter.notifyDataSetChanged();

        resetRefresh();
    }

    @Override
    public void emptyTeamTodoList() {
        Toast.makeText(TeamTodoListActivity.this, "첫 번째 할 일을 생성해보세요.", Toast.LENGTH_SHORT).show();

        resetRefresh();
    }

    @Override
    public void failedGetTeamTodoList() {
        Toast.makeText(TeamTodoListActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();

        resetRefresh();
    }

    private void resetRefresh() {
        if (srlTeamTodoList.isRefreshing()) {
            srlTeamTodoList.setRefreshing(false);
        }
    }

    @Override
    public void updatedTodoState(TodoEntity data, int position) {
        if (data.getTodoState().equals(TODO_STATE_CONFIRMED)) {
            teamTodoListAdapter.removeItem(position);
            teamTodoListAdapter.notifyItemRemoved(position);

            setTodoCompleted(data);
        } else {
            teamTodoListAdapter.setItem(position, data);
            teamTodoListAdapter.notifyItemChanged(position);
        }
    }

    private void setTodoCompleted(TodoEntity data) {
        teamTodoDataList.remove(data);
    }

    @Override
    public void failedUpdateTodoState() {
        Toast.makeText(this, "할 일을 갱신하는 데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }
}