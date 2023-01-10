package com.hero.seoultechteams.view.main.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ItemNotificationListBinding;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.List;


public class AccountNotificationAdapter extends BaseAdapter<AccountNotificationAdapter.AccountNotificationViewHolder, TodoEntity> implements View.OnClickListener{
    private final Context context;
    private final List<TodoEntity> accountNotificationDataList;

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
    public void onClick(View view) {

    }

    class AccountNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemNotificationListBinding binding;

        public AccountNotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNotificationListBinding.bind(itemView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, accountNotificationDataList.get(position));
        }
    }
}
