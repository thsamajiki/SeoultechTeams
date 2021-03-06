package com.hero.seoultechteams.view.main.team.option_menu;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeamMemberListAdapter extends BaseAdapter<TeamMemberListAdapter.TeamMemberViewHolder, MemberData> {

    private Context context;
    private ArrayList<MemberData> teamMemberDataList;
    private LayoutInflater inflater;
    private RequestManager requestManager;
    private String leaderKey;

    public interface OnMemberProfileImageClickListener {
        void profileImageOnClick(String profileImageUrl);
    }

    private OnMemberProfileImageClickListener onMemberProfileImageClickListener;

    public TeamMemberListAdapter(Context context, ArrayList<MemberData> teamMemberDataList, OnMemberProfileImageClickListener onMemberProfileImageClickListener) {
        this.context = context;
        this.teamMemberDataList = teamMemberDataList;
        this.onMemberProfileImageClickListener = onMemberProfileImageClickListener;
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
        MemberData memberData = teamMemberDataList.get(position);

        if (TextUtils.isEmpty(memberData.getProfileImageUrl())) {
            requestManager.load(R.drawable.sample_profile_image).into(holder.ivMemberProfile);
        } else {
            requestManager.load(memberData.getProfileImageUrl()).into(holder.ivMemberProfile);
        }

        holder.ivMemberProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMemberProfileImageClickListener.profileImageOnClick(memberData.getProfileImageUrl());
            }
        });

        holder.tvMemberName.setText(memberData.getName());
        holder.tvMemberEmail.setText(memberData.getEmail());

        boolean isLeader = leaderKey.equals(memberData.getKey());
        if (isLeader) {
            holder.ivTeamLeader.setVisibility(View.VISIBLE);
        } else {
            holder.ivTeamLeader.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return teamMemberDataList.size();
    }

    class TeamMemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView ivMemberProfile;
        private TextView tvMemberName, tvMemberEmail;
        private ImageView ivTeamLeader;
        private RelativeLayout rlItemTeamMemberList;

        public TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMemberProfile = itemView.findViewById(R.id.iv_member_profile);
            tvMemberName = itemView.findViewById(R.id.tv_member_name);
            tvMemberEmail = itemView.findViewById(R.id.tv_searched_user_email);
            ivTeamLeader = itemView.findViewById(R.id.iv_team_leader);
            rlItemTeamMemberList = itemView.findViewById(R.id.rl_item_team_member_list);
            rlItemTeamMemberList.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, teamMemberDataList.get(position));
        }
    }
}