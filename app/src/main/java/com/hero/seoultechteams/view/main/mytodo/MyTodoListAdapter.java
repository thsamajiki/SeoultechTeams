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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.utils.TimeUtils;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyTodoListAdapter extends BaseAdapter<MyTodoListAdapter.MyTodoListViewHolder, TodoEntity> implements View.OnClickListener {

    private Context context;
    private LayoutInflater inflater;
    private List<TodoEntity> myTodoDataList;
    private String myKey;
    private List<TeamEntity> myTeamDataList = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.item_mytodo_list, parent, false);
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
        TodoEntity todoData = myTodoDataList.get(position);
        holder.tvMyTodoTitle.setText(todoData.getTodoTitle());

        if (TextUtils.isEmpty(todoData.getTodoDesc())) {
            holder.tvMyTodoDesc.setVisibility(View.GONE);
        } else {
            holder.tvMyTodoDesc.setVisibility(View.VISIBLE);
            holder.tvMyTodoDesc.setText(todoData.getTodoDesc());
        }

        holder.tvMyTodoTeamName.setText(todoData.getTeamName());

        holder.tvMyTodoStartDate.setText(TimeUtils.getInstance().convertTimeFormat(todoData.getTodoCreatedTime(), "MM월dd일"));
        holder.tvMyTodoEndDate.setText(TimeUtils.getInstance().convertTimeFormat(todoData.getTodoEndTime(), "MM월dd일 HH:mm 까지"));

        long todoInterval = todoData.getTodoEndTime() - todoData.getTodoCreatedTime();
        long todayInterval = System.currentTimeMillis() - todoData.getTodoCreatedTime();
        long oneDay = 24 * 60 * 60 * 1000;
        int todoIntervalDate = (int) (todoInterval / oneDay);
        int todayIntervalDate = (int) (todayInterval / oneDay);
        if (todoInterval <= 0) {
            holder.pbMyTodoDDay.setProgress(100);
        } else {
            int percent = (int) (((double) todayIntervalDate / (double) todoIntervalDate) * 100);

            if (percent < 0) {
                holder.pbMyTodoDDay.setProgress(100);
            } else {
                holder.pbMyTodoDDay.setProgress(percent);
            }
        }

        setTodoState(holder, todoData, isLeader(todoData));
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
        holder.btnDismissMyTodo.setVisibility(View.GONE);
        switch (todoState) {
            case TODO_STATE_IN_PROGRESS:
                if (todoEntity.getTodoEndTime() < System.currentTimeMillis()) {
                    holder.btnStateMyTodo.setText("지연제출");    // Todo의 버튼은 "지연 제출"로 바뀐다.
                    holder.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryYellow)))));
                    holder.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryYellow30));
                    holder.btnStateMyTodo.setClickable(true);
                } else {    // Todo의 마감시간이 현재 시간보다 크면
                    holder.btnStateMyTodo.setText("제출");  // Todo의 버튼은 "제출"로 바뀐다.
                    holder.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                    holder.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                    holder.btnStateMyTodo.setClickable(true);
                }
                break;
            case TODO_STATE_DISMISSED:
                holder.btnStateMyTodo.setText("다시제출");
                holder.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                holder.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                holder.btnStateMyTodo.setClickable(true);
                break;
            case TODO_STATE_CONFIRMED:
                holder.btnStateMyTodo.setText("승인됨");
                holder.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                holder.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                holder.btnStateMyTodo.setClickable(false);
                break;
            case TODO_STATE_SUBMITTED:
                if (isLeader) {
                    holder.btnDismissMyTodo.setVisibility(View.VISIBLE);
                    holder.btnDismissMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                    holder.btnDismissMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                    holder.btnStateMyTodo.setText("승인");
                    holder.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                    holder.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                    holder.btnStateMyTodo.setClickable(true);
                } else {
                    holder.btnStateMyTodo.setText("대기중");
                    holder.btnStateMyTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.gray_333)))));
                    holder.btnStateMyTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_gray));
                    holder.btnStateMyTodo.setClickable(false);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myTodoDataList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class MyTodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvMyTodoTitle, tvMyTodoDesc, tvMyTodoTeamName, tvMyTodoStartDate, tvMyTodoEndDate;
        private MaterialCardView mcvMyTodoList;
        ImageView btnMyTodoOptionMenu;
        ProgressBar pbMyTodoDDay;
        private MaterialButton btnDismissMyTodo, btnStateMyTodo;

        public MyTodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            setOnClickListener();
        }

        private void setOnClickListener() {
            mcvMyTodoList.setOnClickListener(this);
            btnDismissMyTodo.setOnClickListener(this);
            btnStateMyTodo.setOnClickListener(this);
            btnMyTodoOptionMenu.setOnClickListener(this);
        }

        private void initView(View itemView) {
            tvMyTodoTitle = itemView.findViewById(R.id.tv_mytodo_title);
            tvMyTodoDesc = itemView.findViewById(R.id.tv_mytodo_desc);
            tvMyTodoStartDate = itemView.findViewById(R.id.tv_mytodo_start_date);
            tvMyTodoEndDate = itemView.findViewById(R.id.tv_mytodo_end_date);
            tvMyTodoTeamName = itemView.findViewById(R.id.tv_mytodo_team_name);
            btnMyTodoOptionMenu = itemView.findViewById(R.id.btn_mytodo_option_menu);
            mcvMyTodoList = itemView.findViewById(R.id.mcv_mytodo_list);
            pbMyTodoDDay = itemView.findViewById(R.id.pb_mytodo_d_day);
            btnDismissMyTodo = itemView.findViewById(R.id.btn_dismiss_mytodo);
            btnStateMyTodo = itemView.findViewById(R.id.btn_state_mytodo);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, myTodoDataList.get(position));
        }
    }
}