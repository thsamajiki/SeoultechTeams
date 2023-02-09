package com.hero.seoultechteams.view.main.mytodo;

import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_CONFIRMED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_DISMISSED;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_IN_PROGRESS;
import static com.hero.seoultechteams.domain.todo.entity.TodoEntity.TODO_STATE_SUBMITTED;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ItemMyTodoListBinding;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.utils.TimeUtils;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyTodoListAdapter extends BaseAdapter<MyTodoListAdapter.MyTodoListViewHolder, TodoEntity> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<TodoEntity> myTodoDataList;
    private final String myKey;
    private final List<TeamEntity> myTeamDataList = new ArrayList<>();
    public static final String EXTRA_SUBMIT_MY_TODO = "submitMyTodo";
    public static final String EXTRA_SUBMIT_LATE_MY_TODO = "submitLateMyTodo";
    public static final String EXTRA_RESUBMIT_MY_TODO = "resubmitMyTodo";

    public void removeItem(int position) {
        myTodoDataList.remove(position);
    }

    public void setItem(int position, TodoEntity data) {
        myTodoDataList.set(position, data);
    }

    public interface OnBtnStateMyTodoClickListener {
        void btnStateMyTodoOnClick(TodoEntity data);
    }

    private OnBtnStateMyTodoClickListener onBtnStateMyTodoClickListener;

    public MyTodoListAdapter(Context context, List<TodoEntity> myTodoDataList, OnBtnStateMyTodoClickListener onBtnStateMyTodoClickListener) {
        this.context = context;
        this.myTodoDataList = myTodoDataList;
        this.onBtnStateMyTodoClickListener = onBtnStateMyTodoClickListener;
        inflater = LayoutInflater.from(context);
        myKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setMyTeamDataList(List<TeamEntity> myTeamDataList) {
        if (myTeamDataList != null) {
            this.myTeamDataList.clear();
            this.myTeamDataList.addAll(myTeamDataList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyTodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_my_todo_list, parent, false);
        return new MyTodoListViewHolder(view);
    }

    public void setMyTodoListOnTab(List<TodoEntity> myTodoDataList) {
        this.myTodoDataList.clear();
        this.myTodoDataList.addAll(myTodoDataList);
        Collections.sort(myTodoDataList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyTodoListViewHolder holder, int position) {
        TodoEntity todo = myTodoDataList.get(position);

        holder.bind(todo);

        setTodoState(holder, todo, isLeader(todo));
    }

    private boolean isLeader(TodoEntity todoData) {
        for (TeamEntity teamData : myTeamDataList) {
            if (teamData.getTeamKey().equals(todoData.getTeamKey()) && teamData.getLeaderKey().equals(myKey)) {
                return true;
            }
        }
        return false;
    }

    private void setTodoState(MyTodoListViewHolder holder, TodoEntity todoEntity, boolean isLeader) {
        String todoState = todoEntity.getTodoState();
        holder.binding.btnDismissMyTodo.setVisibility(View.GONE);
        switch (todoState) {
            case TODO_STATE_IN_PROGRESS:
                if (todoEntity.getTodoEndTime() < System.currentTimeMillis()) {
                    holder.binding.btnStateMyTodo.setText("지연제출");    // Todo 의 버튼은 "지연 제출"로 바뀐다.
                    holder.binding.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryYellow)))));
                    holder.binding.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryYellow30));
                    holder.binding.btnStateMyTodo.setClickable(true);
                } else {    // Todo 의 마감시간이 현재 시간보다 크면
                    holder.binding.btnStateMyTodo.setText("제출");  // Todo 의 버튼은 "제출"로 바뀐다.
                    holder.binding.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                    holder.binding.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                    holder.binding.btnStateMyTodo.setClickable(true);
                }
                break;
            case TODO_STATE_DISMISSED:
                holder.binding.btnStateMyTodo.setText("다시제출");
                holder.binding.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                holder.binding.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                holder.binding.btnStateMyTodo.setClickable(true);
                break;
            case TODO_STATE_CONFIRMED:
                holder.binding.btnStateMyTodo.setText("승인됨");
                holder.binding.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                holder.binding.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                holder.binding.btnStateMyTodo.setClickable(false);
                break;
            case TODO_STATE_SUBMITTED:
                if (isLeader) {
                    holder.binding.btnDismissMyTodo.setVisibility(View.VISIBLE);
                    holder.binding.btnDismissMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                    holder.binding.btnDismissMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                    holder.binding.btnStateMyTodo.setText("승인");
                    holder.binding.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                    holder.binding.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                    holder.binding.btnStateMyTodo.setClickable(true);
                } else {
                    holder.binding.btnStateMyTodo.setText("대기중");
                    holder.binding.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.gray_333)))));
                    holder.binding.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_gray));
                    holder.binding.btnStateMyTodo.setClickable(false);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myTodoDataList.size();
    }

    class MyTodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemMyTodoListBinding binding;

        public MyTodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMyTodoListBinding.bind(itemView);
            setOnClickListener();
        }

        public void bind(TodoEntity todoItem) {
            binding.tvMyTodoTitle.setText(todoItem.getTodoTitle());

            if (TextUtils.isEmpty(todoItem.getTodoDesc())) {
                binding.tvMyTodoDesc.setVisibility(View.GONE);
            } else {
                binding.tvMyTodoDesc.setVisibility(View.VISIBLE);
                binding.tvMyTodoDesc.setText(todoItem.getTodoDesc());
            }

            binding.tvMyTodoTeamName.setText(todoItem.getTeamName());

            binding.tvMyTodoStartDate.setText(TimeUtils.getInstance().convertTimeFormat(todoItem.getTodoCreatedTime(), "MM월dd일"));
            binding.tvMyTodoEndDate.setText(TimeUtils.getInstance().convertTimeFormat(todoItem.getTodoEndTime(), "MM월dd일 HH:mm 까지"));

            long todoInterval = todoItem.getTodoEndTime() - todoItem.getTodoCreatedTime();
            long todayInterval = System.currentTimeMillis() - todoItem.getTodoCreatedTime();
            long oneDay = 24 * 60 * 60 * 1000;
            int todoIntervalDate = (int) (todoInterval / oneDay);
            int todayIntervalDate = (int) (todayInterval / oneDay);
            if (todoInterval <= 0) {
                binding.pbMyTodoDDay.setProgress(100);
            } else {
                int percent = (int) (((double) todayIntervalDate / (double) todoIntervalDate) * 100);

                if (percent < 0) {
                    binding.pbMyTodoDDay.setProgress(100);
                } else {
                    binding.pbMyTodoDDay.setProgress(percent);
                }
            }
        }

        private void setOnClickListener() {
            binding.mcvMyTodoList.setOnClickListener(this);
            binding.btnDismissMyTodo.setOnClickListener(this);
            binding.btnStateMyTodo.setOnClickListener(this);
            binding.btnMyTodoOptionMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, myTodoDataList.get(position));
        }
    }
}