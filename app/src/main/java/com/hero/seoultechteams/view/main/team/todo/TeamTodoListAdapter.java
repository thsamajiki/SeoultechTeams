package com.hero.seoultechteams.view.main.team.todo;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.todo.TodoRepository;
import com.hero.seoultechteams.database.todo.entity.Event;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.utils.TimeUtils;
import com.hero.seoultechteams.view.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.hero.seoultechteams.database.todo.entity.TodoData.TODO_STATE_CONFIRMED;
import static com.hero.seoultechteams.database.todo.entity.TodoData.TODO_STATE_DISMISSED;
import static com.hero.seoultechteams.database.todo.entity.TodoData.TODO_STATE_IN_PROGRESS;
import static com.hero.seoultechteams.database.todo.entity.TodoData.TODO_STATE_SUBMITTED;


public class TeamTodoListAdapter extends BaseAdapter<TeamTodoListAdapter.TeamTodoListViewHolder, TodoData> {

    private Context context;
    private ArrayList<TodoData> teamTodoDataList;
    private LayoutInflater inflater;
    private RequestManager requestManager;
    private String myKey;
    private boolean isLeader = true;


    public interface OnBtnStateTodoClickListener {
        void btnStateTodoOnClick(TodoData data);
    }

    private OnBtnStateTodoClickListener onBtnStateTodoClickListener;

    public TeamTodoListAdapter(Context context, ArrayList<TodoData> teamTodoDataList, OnBtnStateTodoClickListener onBtnStateTodoClickListener) {
        this.context = context;
        this.teamTodoDataList = teamTodoDataList;
        this.onBtnStateTodoClickListener = onBtnStateTodoClickListener;
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
        TodoData todoData = teamTodoDataList.get(position);
        holder.tvTodoTitle.setText(todoData.getTodoTitle());

        if (TextUtils.isEmpty(todoData.getManagerProfileImageUrl())) {
            requestManager.load(R.drawable.sample_profile_image).into(holder.ivUserProfile);
        } else {
            requestManager.load(todoData.getManagerProfileImageUrl()).into(holder.ivUserProfile);
        }

        holder.tvTodoStartDate.setText(TimeUtils.getInstance().convertTimeFormat(todoData.getTodoCreatedTime(), "MM월dd일"));
        holder.tvTodoEndDate.setText(TimeUtils.getInstance().convertTimeFormat(todoData.getTodoEndTime(), "MM월dd일 HH:mm 까지"));

        String profileImageUri = todoData.getManagerProfileImageUrl();
        if (profileImageUri != null) {
            Glide.with(context).load(profileImageUri).into(holder.ivUserProfile);
        } else {
            holder.ivUserProfile.setImageResource(R.drawable.sample_profile_image);
        }

        holder.tvTodoUserName.setText(todoData.getManagerName());
        holder.tvTodoUserEmail.setText(todoData.getManagerEmail());

        long todoInterval = todoData.getTodoEndTime() - todoData.getTodoCreatedTime();
        long todayInterval = System.currentTimeMillis() - todoData.getTodoCreatedTime();
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

        setTodoState(holder, todoData);
    }

    private long getDismissedTime(TodoData todoData) {
        Collections.sort(todoData.getEventHistory(), new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Long.compare(o2.getTime(), o1.getTime());
            }
        });
        for (Event event : todoData.getEventHistory()) {
            if (event.getEvent().equals(Event.EVENT_DISMISS)) {
                return event.getTime();
            }
        }
        return 0;
    }

    private void setTodoState(TeamTodoListViewHolder holder, TodoData todoData) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String todoState = todoData.getTodoState();
        holder.btnDismissTodo.setVisibility(View.GONE);
        switch (todoState) {
            case TODO_STATE_IN_PROGRESS:
                if (todoData.getTodoEndTime() < System.currentTimeMillis()) {
                    if (todoData.getUserKey().equals(firebaseUser.getUid())) {
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
                    if (todoData.getUserKey().equals(firebaseUser.getUid())) {
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
                if (todoData.getUserKey().equals(firebaseUser.getUid())) {
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
        return teamTodoDataList.size();
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
            switch (view.getId()) {
//                case R.id.btn_todo_option_menu:
//                    openTodoOptionMenu();
//                    break;
                case R.id.btn_dismiss_todo:
                    updateTodo(true, getAdapterPosition());
                    break;
                case R.id.btn_state_todo:
                    updateTodo(false, getAdapterPosition());
                    break;
                default:
                    int position = getAdapterPosition();
                    getOnRecyclerItemClickListener().onItemClick(position, view, teamTodoDataList.get(position));
                    break;
            }
        }

        private void updateTodo(boolean isDismissed, int position) {
            TodoData todoData = teamTodoDataList.get(position);
            Event newEvent = new Event();
            switch (todoData.getTodoState()) {
                case TODO_STATE_IN_PROGRESS:
                case TODO_STATE_DISMISSED:
                    todoData.setTodoState(TODO_STATE_SUBMITTED);
                    newEvent.setEvent(Event.EVENT_SUBMIT);
                    break;
                case TODO_STATE_SUBMITTED:
                    if (isDismissed) {
                        todoData.setTodoState(TODO_STATE_DISMISSED);
                        newEvent.setEvent(Event.EVENT_DISMISS);
                    } else {
                        todoData.setTodoState(TODO_STATE_CONFIRMED);
                        newEvent.setEvent(Event.EVENT_CONFIRM);
                    }
                    break;

            }

            newEvent.setTime(System.currentTimeMillis());
            todoData.getEventHistory().add(newEvent);
            TodoRepository todoRepository = new TodoRepository(context);
            todoRepository.updateTodo(new OnCompleteListener<TodoData>() {
                @Override
                public void onComplete(boolean isSuccess, TodoData data) {
                    if (isSuccess) {
                        teamTodoDataList.set(position, data);
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, "데이터 처리에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, todoData);
        }


//        private void openTodoOptionMenu() {
//            PopupMenu popupMenu = new PopupMenu(context, btnTodoOptionMenu);
//            popupMenu.getMenuInflater().inflate(R.menu.menu_team_todo_option, popupMenu.getMenu());
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch(item.getItemId()) {
//                        case R.id.menu_delete_team_todo :
//                            openDeleteTeamTodoDialog();
//                            break;
//                    }
//                    return true;
//                }
//            });
//            popupMenu.show();
//        }
//
//        private void openDeleteTeamTodoDialog() {
//            String delete_todo_message = "할 일을 삭제하시겠습니까?";
//            String positiveText = "예";
//            String negativeText = "아니오";
//            new MaterialAlertDialogBuilder(context).setMessage(delete_todo_message)
//                    .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //removeTeamTodo();
//                        }
//                    })
//                    .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .create()
//                    .show();
//        }

        private void removeTeamTodo() {
            TodoRepository todoRepository = new TodoRepository(context);
            todoRepository.removeTodo(new OnCompleteListener<TodoData>() {
                @Override
                public void onComplete(boolean isSuccess, TodoData data) {
                    if (isSuccess) {
                        Toast.makeText(context, "할 일을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "할 일을 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, teamTodoDataList.get(getAdapterPosition()));
        }
    }
}