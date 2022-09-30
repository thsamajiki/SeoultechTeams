package com.hero.seoultechteams.view.main.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.card.MaterialCardView;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.ArrayList;


public class TeamListAdapter extends BaseAdapter<TeamListAdapter.TeamListViewHolder, TeamEntity> {

    private Context context;
    private ArrayList<TeamEntity> teamDataList;
    private LayoutInflater inflater;
    private RequestManager requestManager;
    private String myUserKey;
    public static final int UPDATE_TEAM_REQ_CODE = 222;

    public interface OnPopupClickListener {
        void popupOnClick(TeamEntity teamData);
    }

    private OnPopupClickListener onPopupClickListener;

    public TeamListAdapter(Context context, ArrayList<TeamEntity> teamDataList, OnPopupClickListener onPopupClickListener) {
        this.context = context;
        this.teamDataList = teamDataList;
        this.onPopupClickListener = onPopupClickListener;
        inflater = LayoutInflater.from(context);
        requestManager = Glide.with(context);
    }

    @NonNull
    @Override
    public TeamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_list, parent, false);
        return new TeamListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListViewHolder holder, int position) {
        TeamEntity teamData = teamDataList.get(position);

        holder.tvTeamName.setText(teamData.getTeamName());
        holder.tvTeamDesc.setText(teamData.getTeamDesc());

        holder.ivTeamOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeamOptionMenu(holder, teamData);
            }
        });
    }

    private void openTeamOptionMenu(@NonNull TeamListViewHolder holder, TeamEntity teamData) {
        PopupMenu popupMenu = new PopupMenu(context, holder.ivTeamOptionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_team_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menu_modify_team:
                        onPopupClickListener.popupOnClick(teamData);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return teamDataList.size();
    }

    class TeamListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTeamName, tvTeamDesc;
        ImageView ivTeamOptionMenu;
        MaterialCardView mcvTeamList;

        public TeamListViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvTeamName = itemView.findViewById(R.id.tv_team_name);
            tvTeamDesc = itemView.findViewById(R.id.tv_team_desc);
            ivTeamOptionMenu = itemView.findViewById(R.id.iv_team_option_menu);
            mcvTeamList = itemView.findViewById(R.id.mcv_team_list);

            mcvTeamList.setOnClickListener(this);
            ivTeamOptionMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, teamDataList.get(position));
        }
    }
}