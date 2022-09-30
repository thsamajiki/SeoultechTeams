package com.hero.seoultechteams.view.main.team;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.team.contract.TeamListContract;
import com.hero.seoultechteams.view.main.team.option_menu.TeamParticipationActivity;
import com.hero.seoultechteams.view.main.team.presenter.TeamListPresenter;
import com.hero.seoultechteams.view.main.team.todo.TeamTodoListActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.hero.seoultechteams.view.main.team.CreateTeamActivity.EXTRA_CREATE_TEAM;
import static com.hero.seoultechteams.view.main.team.TeamDetailActivity.EXTRA_UPDATE_TEAM;


public class TeamListFragment extends Fragment implements View.OnClickListener, OnRecyclerItemClickListener<TeamEntity>, TeamListContract.View {

    private RecyclerView rvTeamList;
    private FloatingActionButton btnCreateTeam;
    private TeamListAdapter teamListAdapter;
    private ArrayList<TeamEntity> teamDataList = new ArrayList<>();
    private static final int CREATE_TEAM_REQ_CODE = 333;
    private static final int UPDATE_TEAM_DETAIL_REQ_CODE = 444;
    public static final String EXTRA_TEAM_DATA = "teamData";

    private final ActivityResultLauncher<Intent> addTeamLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        TeamData teamData = data.getParcelableExtra(EXTRA_CREATE_TEAM);
                        if (teamData != null) {
                            teamDataList.add(0, teamData.toEntity());
                            teamListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> updateTeamDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == RESULT_OK && data != null) {
                        TeamData teamData = data.getParcelableExtra(EXTRA_UPDATE_TEAM);
                        int index = teamDataList.indexOf(teamData);
                        if (index != -1) {
                            teamDataList.set(index, teamData.toEntity());
                            teamListAdapter.notifyItemChanged(index);
                        }
                    }
                }
            }
    );

    private final TeamListContract.Presenter presenter = new TeamListPresenter(this,
            Injector.getInstance().provideGetTeamListUseCase());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);
        initView(view);
        initTeamListAdapter();
        presenter.getTeamListFromDatabase();
        return view;
    }

    private void initView(View view) {
        rvTeamList = view.findViewById(R.id.rv_team_list);
        btnCreateTeam = view.findViewById(R.id.btn_create_team);
        btnCreateTeam.setOnClickListener(this);
    }

    private void initTeamListAdapter() {
        teamListAdapter = new TeamListAdapter(requireActivity(), teamDataList, new TeamListAdapter.OnPopupClickListener() {
            @Override
            public void popupOnClick(TeamEntity data) {
                Intent intent = new Intent(requireActivity(), TeamDetailActivity.class);
                intent.putExtra(EXTRA_TEAM_DATA, data);
                startActivityForResult(intent, UPDATE_TEAM_DETAIL_REQ_CODE);
            }
        });
        teamListAdapter.setOnRecyclerItemClickListener(this);
        teamListAdapter.notifyDataSetChanged();
        rvTeamList.setAdapter(teamListAdapter);
    }

//    private void getTeamListFromDatabase() {
//        TeamRepositoryImpl teamRepository = new TeamRepositoryImpl(requireActivity());
//        teamRepository.getTeamList(new OnCompleteListener<ArrayList<TeamData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<TeamData> data) {
//                if (isSuccess && data != null) {
//                    teamDataList.clear();
//                    teamDataList.addAll(data);
//                    teamListAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(requireActivity(), "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    public void onItemClick(int position, View view, TeamEntity data) {
        switch (view.getId()) {
            case R.id.iv_team_option_menu:
                //openTeamOptionMenu(data);
                break;
            default:
                intentTeamTodoList(data);
                //intentTeamParticipation(data);
                break;
        }
    }

    private void intentTeamTodoList(TeamEntity data) {
        Intent intent = new Intent(requireActivity(), TeamTodoListActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, data);
        startActivity(intent);
    }

    private void intentTeamParticipation(TeamEntity data) {
        Intent intent = new Intent(requireActivity(), TeamParticipationActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, data);
        startActivity(intent);
    }

    private void openTeamOptionMenu(TeamEntity teamData) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), requireView());
        popupMenu.getMenuInflater().inflate(R.menu.menu_team_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_modify_team:
                        intentTeamDetail(teamData);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void intentTeamDetail(TeamEntity teamData) {
        Intent intent = new Intent(requireActivity(), TeamDetailActivity.class);
        intent.putExtra(EXTRA_TEAM_DATA, teamData);
        updateTeamDetailLauncher.launch(intent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_create_team:
                intentCreateTeam();
                break;
        }
    }

    private void intentCreateTeam() {
        Intent intent = new Intent(requireActivity(), CreateTeamActivity.class);
        addTeamLauncher.launch(intent);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_team_option, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.menu_modify_team :
//                intentTeamDetail();
//                break;
//            case R.id.menu_delete_team :
//                deleteTeam();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void intentTeamDetailAct() {
        Intent intent = new Intent(requireActivity(), TeamDetailActivity.class);
        //intent.putExtra(EXTRA_TEAM_DATA, getTeamData());
        startActivityForResult(intent, UPDATE_TEAM_DETAIL_REQ_CODE);
    }

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
    }

    @Override
    public void onEmptyTeamList() {
        Toast.makeText(requireActivity(), "첫 번째 팀을 생성해보세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedGetTeamList() {
        Toast.makeText(requireActivity(), "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}