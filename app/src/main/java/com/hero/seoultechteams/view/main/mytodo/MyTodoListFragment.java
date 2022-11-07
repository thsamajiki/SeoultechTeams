package com.hero.seoultechteams.view.main.mytodo;

import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_CONFIRMED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_DISMISSED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_IN_PROGRESS;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_SUBMITTED;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.mytodo.contract.MyTodoListContract;
import com.hero.seoultechteams.view.main.mytodo.presenter.MyTodoListPresenter;
import com.hero.seoultechteams.view.main.team.todo.TodoDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class MyTodoListFragment extends Fragment implements OnRecyclerItemClickListener<TodoEntity>, SwipeRefreshLayout.OnRefreshListener, MyTodoListContract.View {

    private TabLayout tlMyTodoList;
    private TabItem tabNowMyTodo, tabCompletedMyTodo;
    private RecyclerView rvMyTodoList;
    private final List<TodoEntity> myTodoNowDataList = new ArrayList<>();
    private final List<TodoEntity> myTodoCompletedDataList = new ArrayList<>();
    private MyTodoListAdapter myTodoListAdapter;
    private SwipeRefreshLayout srlMyTodoList;


    private final MyTodoListContract.Presenter presenter =
            new MyTodoListPresenter(
                    this,
                    Injector.getInstance().provideGetMyTodoListUseCase(),
                    Injector.getInstance().provideGetTeamListUseCase(),
                    Injector.getInstance().provideSetRefreshUseCase(),
                    Injector.getInstance().provideUpdateTodoStateUseCase());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytodo_list, container, false);
        initView(view);
        initMyTodoListAdapter();
        showMyTodoListOnSeparatedTabs();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myTodoNowDataList.clear();
        myTodoCompletedDataList.clear();
        presenter.getMyTodoListFromDatabase();
    }

    private void initView(View view) {
        tlMyTodoList = view.findViewById(R.id.tl_mytodo_list);
        tabNowMyTodo = view.findViewById(R.id.tab_now_mytodo);
        tabCompletedMyTodo = view.findViewById(R.id.tab_completed_mytodo);
        rvMyTodoList = view.findViewById(R.id.rv_mytodo_list);
        srlMyTodoList = view.findViewById(R.id.srl_my_todo_list);
        srlMyTodoList.setOnRefreshListener(this);
    }

    private void showMyTodoListOnSeparatedTabs() {
        tlMyTodoList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        myTodoListAdapter.setMyTodoListOnTab(myTodoNowDataList);
                        myTodoListAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        myTodoListAdapter.setMyTodoListOnTab(myTodoCompletedDataList);
                        myTodoListAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initMyTodoListAdapter() {
        myTodoListAdapter = new MyTodoListAdapter(requireActivity(), new ArrayList<TodoEntity>(), new MyTodoListAdapter.OnBtnStateMyTodoClickListener() {
            @Override
            public void btnStateMyTodoOnClick(TodoEntity data) {
                setTodoCompleted(data);
            }
        });
        myTodoListAdapter.setOnRecyclerItemClickListener(this);
        myTodoListAdapter.notifyDataSetChanged();
        rvMyTodoList.setAdapter(myTodoListAdapter);
    }

    private void setTodoCompleted(TodoEntity data) {
        myTodoNowDataList.remove(data);
        myTodoCompletedDataList.add(data);
    }

    private void getTeamListFromDatabase() {
        presenter.getTeamList();
    }

    @Override
    public void setMyTeamDataList(List<TeamEntity> data) {
        myTodoListAdapter.setMyTeamDataList(data);
    }

    @Override
    public void updatedTodoState(TodoEntity data, int position) {
        if (data.getTodoState().equals(TODO_STATE_CONFIRMED)) {
            myTodoListAdapter.removeItem(position);
            myTodoListAdapter.notifyItemRemoved(position);

            setTodoCompleted(data);
        } else {
            myTodoListAdapter.setItem(position, data);
            myTodoListAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void failedUpdateTodo() {
        Toast.makeText(requireActivity(), "할 일을 갱신하는 데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemClick(int position, View view, TodoEntity data) {
        switch (view.getId()) {
            case R.id.btn_dismiss_mytodo:
                updateMyTodo(true, position, data);
                break;
            case R.id.btn_state_mytodo:
                updateMyTodo(false, position, data);
                break;
            default:
                Intent intent = new Intent(requireActivity(), TodoDetailActivity.class);
                intent.putExtra(TodoDetailActivity.EXTRA_TODO_KEY, data.getTodoKey());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        presenter.setRefresh(true);
        myTodoNowDataList.clear();
        myTodoCompletedDataList.clear();
        presenter.getMyTodoListFromDatabase();
    }

    private void updateMyTodo(boolean isDismissed, int position, TodoEntity data) {
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

        presenter.updateTodo(data, position);
    }

    @Override
    public void onGetMyTodoList(List<TodoEntity> data) {
        for (TodoEntity todoEntity : data) {
            if (todoEntity.getTodoState().equals(TodoEntity.TODO_STATE_CONFIRMED)) {
                myTodoCompletedDataList.add(todoEntity);
            } else {
                myTodoNowDataList.add(todoEntity);
            }
        }
        if (tlMyTodoList.getSelectedTabPosition() == 0) {
            myTodoListAdapter.setMyTodoListOnTab(myTodoNowDataList);
        } else {
            myTodoListAdapter.setMyTodoListOnTab(myTodoCompletedDataList);
        }
        getTeamListFromDatabase();

        srlMyTodoList.setRefreshing(false);
    }

    @Override
    public void emptyMyTodoList() {
        Toast.makeText(requireActivity(), "나의 할 일이 아직 없습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedGetMyTodoList() {
        Toast.makeText(requireActivity(), "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}