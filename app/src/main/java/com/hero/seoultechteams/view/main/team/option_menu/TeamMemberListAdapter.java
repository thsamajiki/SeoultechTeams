package com.hero.seoultechteams.view.main.team.option_menu;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ItemTeamMemberListBinding;
import com.hero.seoultechteams.domain.member.entity.MemberEntity;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.List;


public class TeamMemberListAdapter extends BaseAdapter<TeamMemberListAdapter.TeamMemberViewHolder, MemberEntity> {

    private Context context;
    private final List<MemberEntity> teamMemberDataList;
    private final LayoutInflater inflater;
    private final RequestManager requestManager;
    private String leaderKey;

    public interface OnMemberProfileImageClickListener {
        void profileImageOnClick(String profileImageUrl);
    }

    private OnMemberProfileImageClickListener onMemberProfileImageClickListener;

    public void memberCallBack(OnMemberProfileImageClickListener onMemberProfileImageClickListener) {
        this.onMemberProfileImageClickListener = onMemberProfileImageClickListener;
    }

    public TeamMemberListAdapter(Context context, List<MemberEntity> teamMemberDataList) {
        this.context = context;
        this.teamMemberDataList = teamMemberDataList;
        inflater = LayoutInflater.from(context);
        requestManager = Glide.with(context);
    }

    public void setLeaderKey(String leaderKey) {
        this.leaderKey = leaderKey;
    }


    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_member_list, parent, false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        MemberEntity memberEntity = teamMemberDataList.get(position);

        holder.bind(memberEntity);
    }

    @Override
    public int getItemCount() {
        return teamMemberDataList.size();
    }

    class TeamMemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemTeamMemberListBinding binding;

        public TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemTeamMemberListBinding.bind(itemView);
            binding.rlItemTeamMemberList.setOnClickListener(this);
        }

        public void bind(MemberEntity memberItem) {
            if (TextUtils.isEmpty(memberItem.getProfileImageUrl())) {
                requestManager.load(R.drawable.sample_profile_image).into(binding.ivMemberProfile);
            } else {
                requestManager.load(memberItem.getProfileImageUrl()).into(binding.ivMemberProfile);
            }

            binding.tvMemberName.setText(memberItem.getName());
            binding.tvMemberEmail.setText(memberItem.getEmail());

            boolean isLeader = leaderKey.equals(memberItem.getKey());
            if (isLeader) {
                binding.ivTeamLeader.setVisibility(View.VISIBLE);
            } else {
                binding.ivTeamLeader.setVisibility(View.GONE);
            }

            binding.ivMemberProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMemberProfileImageClickListener.profileImageOnClick(memberItem.getProfileImageUrl());
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, teamMemberDataList.get(position));
        }
    }
}