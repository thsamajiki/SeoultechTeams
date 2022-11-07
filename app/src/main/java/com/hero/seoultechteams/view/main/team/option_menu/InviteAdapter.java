package com.hero.seoultechteams.view.main.team.option_menu;

import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.BaseAdapter;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class InviteAdapter extends BaseAdapter<InviteAdapter.InviteViewHolder, UserEntity> {

    private Context context;
    private List<UserEntity> searchedUserDataList = new ArrayList<>();
    private ArrayList<UserEntity> inviteUserDataList = new ArrayList<>();
    private LayoutInflater inflater;
    private RequestManager requestManager;

    public void replaceAll(List<UserEntity> data) {
        searchedUserDataList.clear();
        searchedUserDataList.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<UserEntity> getInviteUserDataList() {
        return inviteUserDataList;
    }

    public interface OnInviteMemberItemCheckListener {
        void inviteMemberOnCheck(UserEntity data);
    }

    private OnInviteMemberItemCheckListener onInviteMemberItemCheckListener;

    public InviteAdapter(Context context) {
        this.context = context;
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
        UserEntity userData = searchedUserDataList.get(position);

        holder.bind(userData);
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
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, inviteUserDataList.get(position));
        }



        private void intentPhoto(String profileImageUrl) {
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
        }

        public void bind(UserEntity userData) {
            if (TextUtils.isEmpty(userData.getProfileImageUrl())) {
                requestManager.load(R.drawable.sample_profile_image).into(ivSearchedUserProfile);
            } else {
                requestManager.load(userData.getProfileImageUrl()).into(ivSearchedUserProfile);
            }

            tvSearchedUserName.setText(userData.getName());
            tvSearchedUserEmail.setText(userData.getEmail());

            chkboxInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chkboxInvite.isChecked()) {
                        inviteUserDataList.add(userData);
                    }
                }
            });
        }
    }
}
