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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.databinding.ItemTeamTodoListBinding;
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

        holder.bind(todoEntity);

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
        holder.binding.btnDismissTodo.setVisibility(View.GONE);
        switch (todoState) {
            case TODO_STATE_IN_PROGRESS:
                if (todoEntity.getTodoEndTime() < System.currentTimeMillis()) {
                    if (todoEntity.getUserKey().equals(firebaseUser.getUid())) {
                        holder.binding.btnStateTodo.setText("지연제출");    // Todo의 버튼은 "지연 제출"로 바뀐다.
                        holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryYellow)))));
                        holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryYellow30));
                        holder.binding.btnStateTodo.setClickable(true);
                    } else {
                        holder.binding.btnStateTodo.setText("진행중");  // Todo의 버튼은 "제출"로 바뀐다.
                        holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                        holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                        holder.binding.btnStateTodo.setClickable(false);
                    }
                } else {    // Todo의 마감시간이 현재 시간보다 크면
                    if (todoEntity.getUserKey().equals(firebaseUser.getUid())) {
                        holder.binding.btnStateTodo.setText("제출");  // Todo의 버튼은 "제출"로 바뀐다.
                        holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                        holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                        holder.binding.btnStateTodo.setClickable(true);
                    } else {
                        holder.binding.btnStateTodo.setText("진행중");  // Todo의 버튼은 "제출"로 바뀐다.
                        holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                        holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                        holder.binding.btnStateTodo.setClickable(false);
                    }
                }
                break;
            case TODO_STATE_DISMISSED:
                if (todoEntity.getUserKey().equals(firebaseUser.getUid())) {
                    holder.binding.btnStateTodo.setText("다시제출");
                    holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                    holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                    holder.binding.btnStateTodo.setClickable(true);
                } else {
                    holder.binding.btnStateTodo.setText("진행중");  // Todo의 버튼은 "제출"로 바뀐다.
                    holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimary)))));
                    holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary30));
                    holder.binding.btnStateTodo.setClickable(false);
                }
                break;
            case TODO_STATE_CONFIRMED:
                holder.binding.btnStateTodo.setText("승인됨");
                holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                holder.binding.btnStateTodo.setClickable(false);
                break;
            case TODO_STATE_SUBMITTED:
                if (isLeader) {
                    holder.binding.btnDismissTodo.setVisibility(View.VISIBLE);
                    holder.binding.btnDismissTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryRed)))));
                    holder.binding.btnDismissTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryRed60));
                    holder.binding.btnStateTodo.setText("승인");
                    holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.colorPrimaryGreen)))));
                    holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryGreen30));
                    holder.binding.btnStateTodo.setClickable(true);
                } else {
                    holder.binding.btnStateTodo.setText("대기중");
                    holder.binding.btnStateTodo.setTextColor(Color.parseColor(context.getString(Integer.parseInt(String.valueOf(R.color.gray_333)))));
                    holder.binding.btnStateTodo.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_gray));
                    holder.binding.btnStateTodo.setClickable(false);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return teamTodoEntityList.size();
    }

    class TeamTodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemTeamTodoListBinding binding;

        public TeamTodoListViewHolder(View itemView) {
            super(itemView);
            binding = ItemTeamTodoListBinding.bind(itemView);
            setOnClickListener();
        }

        public void bind(TodoEntity todoItem) {
            binding.tvTodoTitle.setText(todoItem.getTodoTitle());

            if (TextUtils.isEmpty(todoItem.getManagerProfileImageUrl())) {
                requestManager.load(R.drawable.sample_profile_image).into(binding.ivMemberProfile);
            } else {
                requestManager.load(todoItem.getManagerProfileImageUrl()).into(binding.ivMemberProfile);
            }

            binding.tvTodoStartDate.setText(TimeUtils.getInstance().convertTimeFormat(todoItem.getTodoCreatedTime(), "MM월dd일"));
            binding.tvTodoEndDate.setText(TimeUtils.getInstance().convertTimeFormat(todoItem.getTodoEndTime(), "MM월dd일 HH:mm 까지"));

            String profileImageUri = todoItem.getManagerProfileImageUrl();
            if (profileImageUri != null) {
                Glide.with(context).load(profileImageUri).into(binding.ivMemberProfile);
            } else {
                binding.ivMemberProfile.setImageResource(R.drawable.sample_profile_image);
            }

            binding.tvTodoUserName.setText(todoItem.getManagerName());
            binding.tvTodoUserEmail.setText(todoItem.getManagerEmail());

            long todoInterval = todoItem.getTodoEndTime() - todoItem.getTodoCreatedTime();
            long todayInterval = System.currentTimeMillis() - todoItem.getTodoCreatedTime();
            long oneDay = 24 * 60 * 60 * 1000;
            int todoIntervalDate = (int) (todoInterval / oneDay);
            int todayIntervalDate = (int) (todayInterval / oneDay);
            if (todoInterval <= 0) {
                binding.piTodoDDay.setProgress(100);
            } else {
                int percent = (int) (((double) todayIntervalDate / (double) todoIntervalDate) * 100);

                if (percent < 0) {
                    binding.piTodoDDay.setProgress(100);
                } else {
                    binding.piTodoDDay.setProgress(percent);
                }
            }
        }

        private void setOnClickListener() {
            binding.mcvTeamTodoList.setOnClickListener(this);
            binding.btnTodoOptionMenu.setOnClickListener(this);
            binding.btnDismissTodo.setOnClickListener(this);
            binding.btnStateTodo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            getOnRecyclerItemClickListener().onItemClick(position, view, teamTodoEntityList.get(position));
        }
    }
}