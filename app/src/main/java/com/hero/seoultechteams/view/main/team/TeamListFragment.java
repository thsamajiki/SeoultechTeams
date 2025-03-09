package com.hero.seoultechteams.view.main.team;

import static android.app.Activity.RESULT_OK;
import static com.hero.seoultechteams.view.main.team.CreateTeamActivity.EXTRA_CREATE_TEAM;
import static com.hero.seoultechteams.view.main.team.TeamDetailActivity.EXTRA_TEAM_KEY;
import static com.hero.seoultechteams.view.main.team.TeamDetailActivity.EXTRA_UPDATE_TEAM;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hero.seoultechteams.BaseFragment;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.FragmentTeamListBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.contract.TeamListContract;
import com.hero.seoultechteams.view.main.team.presenter.TeamListPresenter;
import com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity;

import java.util.ArrayList;
import java.util.List;


public class TeamListFragment extends BaseFragment<FragmentTeamListBinding> implements View.OnClickListener, OnRecyclerItemClickListener<TeamEntity>, TeamListContract.View {

    private TeamListAdapter teamListAdapter;
    private final List<TeamEntity> teamDataList = new ArrayList<>();
    public static final String EXTRA_TEAM_DATA = "teamData";
    private final TeamListContract.Presenter presenter = new TeamListPresenter(this,
            Injector.getInstance().provideGetTeamListUseCase());

    @NonNull
    @Override
    protected FragmentTeamListBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentTeamListBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTeamListAdapter();
        presenter.getTeamListFromDatabase();
        setOnClickListeners();

        BottomNavigationView nav = getActivity().findViewById(R.id.main_bottom_nav);
        if (nav != null) {
            nav.setOnItemReselectedListener(new BottomNavigationView.OnItemReselectedListener() {   
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.menu_team) {
                        binding.rvTeamList.smoothScrollToPosition(0);
                    }
                }
            });
        }
    }

    private void initTeamListAdapter() {
        teamListAdapter = new TeamListAdapter(requireActivity(), teamDataList);
        teamListAdapter.teamCallBack(new TeamListAdapter.OnPopupClickListener() {
            @Override
            public void popupOnClick(TeamEntity teamData) {

            }
        });

        teamListAdapter.setOnRecyclerItemClickListener(this);
        teamListAdapter.notifyDataSetChanged();
        binding.rvTeamList.setAdapter(teamListAdapter);
    }

    private void setOnClickListeners() {
        binding.btnCreateTeam.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position, View view, TeamEntity data) {
        if (view.getId() == R.id.iv_team_option_menu) {
            openTeamOptionMenu(data, view);
        } else {
            onTeamListItemClick(data);
        }
    }

    private void onTeamListItemClick(TeamEntity data) {
        Intent intent = new Intent(requireActivity(), TeamTodoListActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, data);
        startActivity(intent);
    }

    private void openTeamOptionMenu(TeamEntity teamData, View anchorView) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_team_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_modify_team) {
                    onModifyTeamMenuClick(teamData);
                }

                return true;
            }
        });
        popupMenu.show();
    }

    private void onModifyTeamMenuClick(TeamEntity teamData) {
        Intent intent = new Intent(requireActivity(), TeamDetailActivity.class);
        intent.putExtra(EXTRA_TEAM_KEY, teamData.getTeamKey());
        updateTeamDetailLauncher.launch(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_create_team) {
            onCreateTeamButtonClick();
        }
    }

    private void onCreateTeamButtonClick() {
        Intent intent = new Intent(requireActivity(), CreateTeamActivity.class);
        addTeamLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent>
            addTeamLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        TeamEntity teamEntity = data.getParcelableExtra(EXTRA_CREATE_TEAM);
                        if (teamEntity != null) {
                            teamDataList.add(0, teamEntity);
                            teamListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent>
            updateTeamDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        TeamEntity teamEntity = data.getParcelableExtra(EXTRA_UPDATE_TEAM);
                        int index = -1;

                        for (int i = 0; i < teamDataList.size(); i++) {
                            TeamEntity entity = teamDataList.get(i);
                            if (entity.getTeamKey().equals(teamEntity.getTeamKey())) {
                                index = i;
                                break;
                            }
                        }

                        if (index != -1) {
                            teamDataList.set(index, teamEntity);
                            teamListAdapter.notifyItemChanged(index);
                        }
                    }
                }
            }
    );

    private void deleteTeam() {
        String askIfDelete = "팀을 삭제하시겠습니까?";
        String yesDelete = "예";
        String noDelete = "아니오";
        new MaterialAlertDialogBuilder(requireActivity()).setMessage(askIfDelete)
                .setPositiveButton(yesDelete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(noDelete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onGetTeamList(List<TeamEntity> data) {
        teamDataList.clear();
        teamDataList.addAll(data);
        teamListAdapter.notifyDataSetChanged();

        binding.tvCreateFirstTeam.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onEmptyTeamList() {
        binding.tvCreateFirstTeam.setVisibility(View.VISIBLE);
//        Toast.makeText(requireActivity(), "첫 번째 팀을 생성해보세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedGetTeamList() {
        Toast.makeText(requireActivity(), "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}