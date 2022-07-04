package com.hero.seoultechteams.view.main.mytodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.team.TeamRepository;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.TodoRepository;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.todo.TodoDetailActivity;

import java.util.ArrayList;

import static com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity.EXTRA_TODO_DATA;


public class MyTodoListFragment extends Fragment implements OnRecyclerItemClickListener<TodoData>, SwipeRefreshLayout.OnRefreshListener {

    private TabLayout tlMyTodoList;
    private TabItem tabNowMyTodo, tabCompletedMyTodo;
    private RecyclerView rvMyTodoList;
    private ArrayList<TodoData> myTodoNowDataList = new ArrayList<>();
    private ArrayList<TodoData> myTodoCompletedDataList = new ArrayList<>();
    private MyTodoListAdapter myTodoListAdapter;
    private SwipeRefreshLayout srlMyTodoList;
    private TodoRepository todoRepository;

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
        getMyTodoListFromDatabase();
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
        myTodoListAdapter = new MyTodoListAdapter(requireActivity(), new ArrayList<TodoData>(), new MyTodoListAdapter.OnBtnStateMyTodoClickListener() {
            @Override
            public void btnStateMyTodoOnClick(TodoData data) {
                myTodoNowDataList.remove(data);
                myTodoCompletedDataList.add(data);
            }
        });
        myTodoListAdapter.setOnRecyclerItemClickListener(this);
        myTodoListAdapter.notifyDataSetChanged();
        rvMyTodoList.setAdapter(myTodoListAdapter);
    }

    private void getMyTodoListFromDatabase() {
        if (todoRepository == null) {
            todoRepository = new TodoRepository(requireActivity());
        }
        String myUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myTodoNowDataList.clear();
        myTodoCompletedDataList.clear();

        todoRepository.getMyTodoList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                if (isSuccess && data != null) {
                    for (TodoData todoData: data) {
                        if (todoData.getTodoState().equals(TodoData.TODO_STATE_CONFIRMED)) {
                            myTodoCompletedDataList.add(todoData);
                        } else {
                            myTodoNowDataList.add(todoData);
                        }
                    }
                    if (tlMyTodoList.getSelectedTabPosition() == 0) {
                        myTodoListAdapter.setMyTodoListOnTab(myTodoNowDataList);
                    } else {
                        myTodoListAdapter.setMyTodoListOnTab(myTodoCompletedDataList);
                    }
                    getTeamListFromDatabase();
                } else {
                    Toast.makeText(requireActivity(), "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                srlMyTodoList.setRefreshing(false);
            }
        }, myUserKey);
    }

    private void getTeamListFromDatabase() {
        TeamRepository teamRepository = new TeamRepository(requireActivity());
        teamRepository.getTeamList(new OnCompleteListener<ArrayList<TeamData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TeamData> data) {
                myTodoListAdapter.setMyTeamDataList(data);
            }
        });
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_mytodo_option, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.menu_delete_my_todo:
//                openDeleteMyTodoDialog();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void openDeleteMyTodoDialog() {
//        String message = "할 일을 삭제하시겠습니까?";
//        String positiveText = "예";
//        String negativeText = "아니오";
//        new MaterialAlertDialogBuilder(requireActivity()).setMessage(message)
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

    @Override
    public void onItemClick(int position, View view, TodoData data) {
        Intent intent = new Intent(requireActivity(), TodoDetailActivity.class);
        intent.putExtra(EXTRA_TODO_DATA, data);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        todoRepository.setRefresh(true);
        getMyTodoListFromDatabase();
    }
}