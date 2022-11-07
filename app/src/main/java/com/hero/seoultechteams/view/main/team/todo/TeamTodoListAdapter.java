package com.hero.seoultechteams.view.main.team.todo;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.utils.TimeUtils;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TeamTodoListAdapter extends BaseAdapter<TeamTodoListAdapter.TeamTodoListViewHolder, TodoEntity> {

    private final Context context;
    private final List<TodoEntity> teamTodoEntityList;
    private final LayoutInflater inflater;
    private final RequestManager requestManager;
    private final String myKey;
    private boolean isLeader = true;

    public void removeItem(int position) {
        teamTodoEntityList.remove(position);
    }

    public void setItem(int position, TodoEntity data) {
        teamTodoEntityList.set(position, data);
    }

    public interface OnBtnStateTodoClickListener {
        void btnStateTodoOnClick(TodoEntity data);
    }

    private OnBtnStateTodoClickListener onBtnStateTodoClickListener;

    public void todoCallBack(OnBtnStateTodoClickListener onBtnStateTodoClickListener) {
        this.onBtnStateTodoClickListener = onBtnStateTodoClickListener;
    }

    public TeamTodoListAdapter(Context context, List<TodoEntity> teamTodoEntityList) {
        this.context = context;
        this.teamTodoEntityList = teamTodoEntityList;
        inflater = LayoutInflater.from(context);
        requestManager = Glide.with(context);
        myKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }



    public void setLeader(boolean leader) {
        isLeader = leader;
    }

    @NonNull
    @Override
    public TeamTodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_todo_list, parent, false);
        return new TeamTodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamTodoListViewHolder holder, int position) {
        TodoEntity todoEntity = teamTodoEntityList.get(position);
        holder.tvTodoTitle.setText(todoEntity.getTodoTitle());

        if (TextUtils.isEmpty(todoEntity.getManagerProfileImageUrl())) {
            requestManager.load(R.drawable.sample_profile_image).into(holder.ivUserProfile);
        } else {
            requestManager.load(todoEntity.getManagerProfileImageUrl()).into(holder.ivUserProfile);
        }

        holder.tvTodoStartDate.setText(TimeUtils.getInstance().convertTimeFormat(todoEntity.getTodoCreatedTime(), "MM월dd일"));
        holder.tvTodoEndDate.setText(TimeUtils.getInstance().convertTimeFormat(todoEntity.getTodoEndTime(), "MM월dd일 HH:mm 까지"));

        String profileImageUri = todoEntity.getManagerProfileImageUrl();
        if (profileImageUri != null) {
            Glide.with(context).load(profileImageUri).into(holder.ivUserProfile);
        } else {
            holder.ivUserProfile.setImageResource(R.drawable.sample_profile_image);
        }

        holder.tvTodoUserName.setText(todoEntity.getManagerName());
        holder.tvTodoUserEmail.setText(todoEntity.getManagerEmail());

        long todoInterval = todoEntity.getTodoEndTime() - todoEntity.getTodoCreatedTime();
        long todayInterval = System.currentTimeMillis() - todoEntity.getTodoCreatedTime();
        long oneDay = 24 * 60 * 60 * 1000;
        int todoIntervalDate = (int) (todoInterval / oneDay);
        int todayIntervalDate = (int) (todayInterval / oneDay);
        if (todoInterval <= 0) {
            holder.progressIndicatorDDay.setProgress(100);
        } else {
            int percent = (int) (((double) todayIntervalDate / (double) todoIntervalDate) * 100);

            if (percent < 0) {
                holder.progressIndicatorDDay.setProgress(100);
            } else {
                holder.progressIndicatorDDay.setProgress(percent);
            }
        }

        setTodoState(holder, todoEntity);
    }

    private long getDismissedTime(TodoEntity todoEntity) {
        Collections.sort(todoEntity.getEventHistory(), new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Long.compare(o2.getTime(), o1.getTime());
            }
        });
        for (Event event : todoEntity.getEventHistory()) {
            if (event.getEvent().equals(Event.EVENT_DISMISS)) {
                return event.getTime();
            }
        }
        return 0;
    }

    private void setTodoState(TeamTodoListViewHolder holder, TodoEntity todoEntity) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String todoState = todoEntity.getTodoState();
        holder.btnDismissTodo.setVisibility(View.GONE);
        switch (todoState) {
            case TODO_STATE_IN_PROGRESS:
                if (todoEntity.getTodoEndTime() < System.currentTimeMillis()) {
                    if (todoEntity.getUserKey().equals(firebaseUser.getUid())) {
                        holder.btnStateTodo.setText("지연제출");    // Todo의 버튼은 "지연 제출"로 바뀐다.
                        holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryYellow)))));
                        holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryYellow30));
                        holder.btnStateTodo.setClickable(true);
                    } else {
                        holder.btnStateTodo.setText("진행중");  // Todo의 버튼은 "제출"로 바뀐다.
                        holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                        holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                        holder.btnStateTodo.setClickable(false);
                    }
                } else {    // Todo의 마감시간이 현재 시간보다 크면
                    if (todoEntity.getUserKey().equals(firebaseUser.getUid())) {
                        holder.btnStateTodo.setText("제출");  // Todo의 버튼은 "제출"로 바뀐다.
                        holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                        holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                        holder.btnStateTodo.setClickable(true);
                    } else {
                        holder.btnStateTodo.setText("진행중");  // Todo의 버튼은 "제출"로 바뀐다.
                        holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                        holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                        holder.btnStateTodo.setClickable(false);
                    }
                }
                break;
            case TODO_STATE_DISMISSED:
                if (todoEntity.getUserKey().equals(firebaseUser.getUid())) {
                    holder.btnStateTodo.setText("다시제출");
                    holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                    holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                    holder.btnStateTodo.setClickable(true);
                } else {
                    holder.btnStateTodo.setText("진행중");  // Todo의 버튼은 "제출"로 바뀐다.
                    holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                    holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                    holder.btnStateTodo.setClickable(false);
                }
                break;
            case TODO_STATE_CONFIRMED:
                holder.btnStateTodo.setText("승인됨");
                holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                holder.btnStateTodo.setClickable(false);
                break;
            case TODO_STATE_SUBMITTED:
                if (isLeader) {
                    holder.btnDismissTodo.setVisibility(View.VISIBLE);
                    holder.btnDismissTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                    holder.btnDismissTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                    holder.btnStateTodo.setText("승인");
                    holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                    holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                    holder.btnStateTodo.setClickable(true);
                } else {
                    holder.btnStateTodo.setText("대기중");
                    holder.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.gray_333)))));
                    holder.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_gray));
                    holder.btnStateTodo.setClickable(false);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return teamTodoEntityList.size();
    }

    class TeamTodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialCardView mcvTeamTodoList;
        TextView tvTodoTitle, tvTodoStartDate, tvTodoEndDate, tvTodoUserName, tvTodoUserEmail;
        ImageView btnTodoOptionMenu, ivUserProfile;
        ProgressBar progressIndicatorDDay;
        MaterialButton btnDismissTodo, btnStateTodo;

        public TeamTodoListViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
            setOnClickListener();
        }

        private void initView(View itemView) {
            mcvTeamTodoList = itemView.findViewById(R.id.mcv_team_todo_list);
            tvTodoTitle = itemView.findViewById(R.id.tv_todo_title);
            btnTodoOptionMenu = itemView.findViewById(R.id.btn_todo_option_menu);
            tvTodoStartDate = itemView.findViewById(R.id.tv_todo_start_date);
            tvTodoEndDate = itemView.findViewById(R.id.tv_todo_end_date);
            progressIndicatorDDay = itemView.findViewById(R.id.pi_todo_d_day);
            ivUserProfile = itemView.findViewById(R.id.iv_member_profile);
            tvTodoUserName = itemView.findViewById(R.id.tv_todo_user_name);
            tvTodoUserEmail = itemView.findViewById(R.id.tv_todo_user_email);
            btnDismissTodo = itemView.findViewById(R.id.btn_dismiss_todo);
            btnStateTodo = itemView.findViewById(R.id.btn_state_todo);
        }

        private void setOnClickListener() {
            mcvTeamTodoList.setOnClickListener(this);
            btnTodoOptionMenu.setOnClickListener(this);
            btnDismissTodo.setOnClickListener(this);
            btnStateTodo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, teamTodoEntityList.get(position));
        }
    }
}