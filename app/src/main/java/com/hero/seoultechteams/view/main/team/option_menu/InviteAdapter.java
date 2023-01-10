package com.hero.seoultechteams.view.main.team.option_menu;

import static com.hero.seoultechteams.view.photoview.PhotoActivity.EXTRA_PROFILE_IMAGE_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ItemInviteListBinding;
import com.hero.seoultechteams.domain.user.entity.UserEntity;
import com.hero.seoultechteams.view.BaseAdapter;
import com.hero.seoultechteams.view.photoview.PhotoActivity;

import java.util.ArrayList;
import java.util.List;


public class InviteAdapter extends BaseAdapter<InviteAdapter.InviteViewHolder, UserEntity> {

    private final Context context;
    private final List<UserEntity> searchedUserDataList = new ArrayList<>();
    private final ArrayList<UserEntity> inviteUserDataList = new ArrayList<>();
    private final LayoutInflater inflater;
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

        private final ItemInviteListBinding binding;

        public InviteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemInviteListBinding.bind(itemView);
            binding.chkboxInvite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, inviteUserDataList.get(position));
        }

        private void intentPhoto(String profileImageUrl) {
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra(EXTRA_PROFILE_IMAGE_URL, profileImageUrl);
            //startActivity(intent);
        }

        public void bind(UserEntity userData) {
//            if (TextUtils.isEmpty(userData.getProfileImageUrl())) {
//                requestManager.load(R.drawable.sample_profile_image).into(ivSearchedUserProfile);
//            } else {
//                requestManager.load(userData.getProfileImageUrl()).into(ivSearchedUserProfile);
//            }

            binding.tvSearchedUserName.setText(userData.getName());
            binding.tvSearchedUserEmail.setText(userData.getEmail());

            binding.chkboxInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.chkboxInvite.isChecked()) {
                        inviteUserDataList.add(userData);
                    }
                }
            });
        }
    }
}
