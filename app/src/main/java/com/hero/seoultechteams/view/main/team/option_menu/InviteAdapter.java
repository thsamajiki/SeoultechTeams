package com.hero.seoultechteams.view.main.team.option_menu;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.view.BaseAdapter;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;


public class InviteAdapter extends BaseAdapter<InviteAdapter.InviteViewHolder, UserData> {

    private Context context;
    private ArrayList<UserData> searchedUserDataList;
    private ArrayList<UserData> inviteUserDataList = new ArrayList<>();
    private LayoutInflater inflater;
    private RequestManager requestManager;

    public interface OnInviteMemberItemCheckListener {
        void inviteMemberOnCheck(UserData data);
    }

    private OnInviteMemberItemCheckListener onInviteMemberItemCheckListener;

    public InviteAdapter(Context context, ArrayList<UserData> searchedUserDataList, OnInviteMemberItemCheckListener onInviteMemberItemCheckListener) {
        this.context = context;
        this.searchedUserDataList = searchedUserDataList;
        this.onInviteMemberItemCheckListener = onInviteMemberItemCheckListener;
        inflater = LayoutInflater.from(context);
        requestManager = Glide.with(context);
    }

    @NonNull
    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_invite_list, parent, false);
        return new InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int position) {
        UserData userData = searchedUserDataList.get(position);

        if (TextUtils.isEmpty(userData.getProfileImageUrl())) {
            requestManager.load(R.drawable.sample_profile_image).into(holder.ivSearchedUserProfile);
        } else {
            requestManager.load(userData.getProfileImageUrl()).into(holder.ivSearchedUserProfile);
        }

        holder.tvSearchedUserName.setText(userData.getName());
        holder.tvSearchedUserEmail.setText(userData.getEmail());

        holder.chkboxInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.chkboxInvite.isChecked()) {
                    onInviteMemberItemCheckListener.inviteMemberOnCheck(userData);
                    inviteUserDataList.add(userData);
                    Log.d("qwer3", "InviteAdapter-onBindViewHolder-체크된 사용자 목록의 크기 : " + inviteUserDataList.size());
                    for (UserData userData : inviteUserDataList) {
                        Log.d("qwer4", "InviteAdapter-onBindViewHolder-체크된 사용자의 이름 : " + userData.getName());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchedUserDataList.size();
    }

    class InviteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView ivSearchedUserProfile;
        private TextView tvSearchedUserName, tvSearchedUserEmail;
        private CheckBox chkboxInvite;
        private RelativeLayout rlItemInviteUserList;

        public InviteViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            ivSearchedUserProfile = itemView.findViewById(R.id.iv_searched_user_profile);
            tvSearchedUserName = itemView.findViewById(R.id.tv_searched_user_name);
            tvSearchedUserEmail = itemView.findViewById(R.id.tv_searched_user_email);
            chkboxInvite = itemView.findViewById(R.id.chkbox_invite);
            rlItemInviteUserList = itemView.findViewById(R.id.rl_item_invite_user_list);

            chkboxInvite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_member_profile:
                    break;
                case R.id.chkbox_invite:
                    addCheckedUsersToList();
                    break;
                default:
                    int position = getAdapterPosition();
                    getOnRecyclerItemClickListener().onItemClick(position, view, inviteUserDataList.get(position));
                    break;
            }
        }

        private void addCheckedUsersToList() {
            UserData userData = new UserData();

            if (chkboxInvite.isChecked()) {
                inviteUserDataList.add(userData);
            }
        }

        private void intentPhoto(String profileImageUrl) {
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
            //startActivity(intent);
        }
    }
}
