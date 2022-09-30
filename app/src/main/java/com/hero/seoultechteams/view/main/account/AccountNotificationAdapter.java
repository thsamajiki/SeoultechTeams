package com.hero.seoultechteams.view.main.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountNotificationAdapter extends BaseAdapter<AccountNotificationAdapter.AccountNotificationViewHolder, TodoEntity> implements View.OnClickListener{
    private Context context;
    private List<TodoEntity> accountNotificationDataList;

    public AccountNotificationAdapter(Context context, List<TodoEntity> accountNotificationDataList) {
        this.context = context;
        this.accountNotificationDataList = accountNotificationDataList;
    }

    @NonNull
    @Override
    public AccountNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_notification_list, parent, false);
        return new AccountNotificationAdapter.AccountNotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountNotificationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return accountNotificationDataList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class AccountNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView ivMyUserProfile;
        TextView tvNotificationTitle, tvNotificationDetail;
        ImageView ivDeleteNotification;

        public AccountNotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationTitle = itemView.findViewById(R.id.tv_notification_title);
            tvNotificationDetail = itemView.findViewById(R.id.tv_notification_detail);
            ivDeleteNotification = itemView.findViewById(R.id.iv_delete_notification);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, v, accountNotificationDataList.get(position));
        }
    }
}
