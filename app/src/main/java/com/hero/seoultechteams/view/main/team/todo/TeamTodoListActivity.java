package com.hero.seoultechteams.view.main.team.todo;

import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_CONFIRMED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_DISMISSED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_IN_PROGRESS;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_SUBMITTED;
import static com.hero.seoultechteams.view.main.team.TeamListFragment.EXTRA_TEAM_DATA;
import static com.hero.seoultechteams.view.main.team.todo.AddTodoActivity.EXTRA_ADD_TODO;
import static com.hero.seoultechteams.view.main.team.todo.TodoDetailActivity.EXTRA_TODO_KEY;
import static com.hero.seoultechteams.view.main.team.todo.TodoDetailActivity.EXTRA_UPDATE_TODO;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.databinding.ActivityTeamTodoListBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.option_menu.TeamMemberListActivity;
import com.hero.seoultechteams.view.main.team.option_menu.TeamParticipationActivity;
import com.hero.seoultechteams.view.main.team.todo.contract.TeamTodoListContract;
import com.hero.seoultechteams.view.main.team.todo.presenter.TeamTodoListPresenter;

import java.util.ArrayList;
import java.util.List;

public class TeamTodoListActivity extends BaseActivity implements View.OnClickListener, OnRecyclerItemClickListener<TodoEntity>, SwipeRefreshLayout.OnRefreshListener, TeamTodoListContract.View {

    private ActivityTeamTodoListBinding binding;
    private final List<TodoEntity> teamTodoDataList = new ArrayList<>();
    private TeamTodoListAdapter teamTodoListAdapter;

    public static final String EXTRA_TEAM_MEMBER_LIST = "teamMemberDataList";

    private final TeamTodoListContract.Presenter presenter = new TeamTodoListPresenter(this,
            Injector.getInstance().provideGetTeamTodoListUseCase(),
            Injector.getInstance().provideUpdateTodoStateUseCase(),
            Injector.getInstance().provideSetRefreshUseCase());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamTodoListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initView();
        initTeamTodoListAdapter();
        presenter.getTeamTodoDataListFromDatabase(getTeamData().getTeamKey());
        setOnClickListener();
    }

    private void initView() {
        binding.srlTeamTodoList.setOnRefreshListener(this);
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
        binding.rvTeamTodoList.setAdapter(teamTodoListAdapter);
    }

    private TeamEntity getTeamData() {
        return getIntent().getParcelableExtra(EXTRA_TEAM_DATA);
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.mcvCreateTodo.setOnClickListener(this);
        binding.ivTeamOptionMenu.setOnClickListener(this);
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
        PopupMenu popupMenu = new PopupMenu(this, binding.ivTeamOptionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_team_todolist_actionbar_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.menu_team_member_list:
                        intent = new Intent(TeamTodoListActivity.this, TeamMemberListActivity.class);
                        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
                        startActivity(intent);
                        break;
                    case R.id.menu_team_participation:
                        intent = new Intent(TeamTodoListActivity.this, TeamParticipationActivity.class);
                        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
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
        intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
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
                intent.putExtra(EXTRA_TODO_KEY, data.getTodoKey());
                updateTodoResultLauncher.launch(intent);
                break;
        }
    }

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
        if (binding.srlTeamTodoList.isRefreshing()) {
            binding.srlTeamTodoList.setRefreshing(false);
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