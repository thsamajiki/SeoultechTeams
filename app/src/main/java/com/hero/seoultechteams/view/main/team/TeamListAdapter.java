package com.hero.seoultechteams.view.main.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ItemTeamListBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.List;

public class TeamListAdapter extends BaseAdapter<TeamListAdapter.TeamListViewHolder, TeamEntity> {

    private final Context context;
    private final List<TeamEntity> teamDataList;
    private final LayoutInflater inflater;
    private final RequestManager requestManager;

    public interface OnPopupClickListener {
        void popupOnClick(TeamEntity teamData);
    }

    private OnPopupClickListener onPopupClickListener;

    public void teamCallBack(OnPopupClickListener onPopupClickListener) {
        this.onPopupClickListener = onPopupClickListener;
    }

    public TeamListAdapter(Context context, List<TeamEntity> teamDataList) {
        this.context = context;
        this.teamDataList = teamDataList;
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

        holder.bind(teamData);
    }

    private void openTeamOptionMenu(@NonNull TeamListViewHolder holder, TeamEntity teamData) {
        PopupMenu popupMenu = new PopupMenu(context, holder.binding.ivTeamOptionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_team_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_modify_team) {
                    onPopupClickListener.popupOnClick(teamData);
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

        private final ItemTeamListBinding binding;

        public TeamListViewHolder(View itemView) {
            super(itemView);
            binding = ItemTeamListBinding.bind(itemView);
            setOnClickListener();
        }

        public void bind(TeamEntity teamItem) {
            binding.tvTeamName.setText(teamItem.getTeamName());
            binding.tvTeamDesc.setText(teamItem.getTeamDesc());
        }

        private void setOnClickListener() {
            binding.mcvTeamList.setOnClickListener(this);
            binding.ivTeamOptionMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, teamDataList.get(position));
        }
    }
}