package com.hero.seoultechteams.database.todo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.domain.todo.entity.TodoEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "todo_table")
public class TodoData implements Comparable<TodoData> {
    public TodoData(String todoTitle, String todoDesc, String userKey, String managerProfileImageUrl, String managerName, String managerEmail, String todoState, String teamName, String teamKey, long todoCreatedTime, long todoEndTime, ArrayList<Event> eventHistory, @NonNull String todoKey) {
        this.todoTitle = todoTitle;
        this.todoDesc = todoDesc;
        this.userKey = userKey;
        this.managerProfileImageUrl = managerProfileImageUrl;
        this.managerName = managerName;
        this.managerEmail = managerEmail;
        this.todoState = todoState;
        this.teamName = teamName;
        this.teamKey = teamKey;
        this.todoCreatedTime = todoCreatedTime;
        this.todoEndTime = todoEndTime;
        this.eventHistory = eventHistory;
        this.todoKey = todoKey;
    }

    public TodoEntity toEntity() {
        return new TodoEntity(todoTitle, todoDesc, userKey, managerProfileImageUrl,
                managerName, managerEmail, todoState, teamName, teamKey,
                todoCreatedTime, todoEndTime, eventHistory, todoKey);
    }

    public static TodoData toData(TodoEntity todoEntity) {
        List<Event> eventHistory;
        if (todoEntity.getEventHistory() != null) {
            eventHistory = todoEntity.getEventHistory();
        } else {
            eventHistory = Collections.emptyList();
        }

        return new TodoData(todoEntity.getTodoTitle(),
                todoEntity.getTodoDesc(),
                todoEntity.getUserKey(),
                todoEntity.getManagerProfileImageUrl(),
                todoEntity.getManagerName(),
                todoEntity.getManagerEmail(),
                todoEntity.getTodoState(),
                todoEntity.getTeamName(),
                todoEntity.getTeamKey(),
                todoEntity.getTodoCreatedTime(),
                todoEntity.getTodoEndTime(),
                new ArrayList<>(eventHistory),
                todoEntity.getTodoKey());
    }

    private String todoTitle;
    private String todoDesc;
    private String userKey;
    private String managerProfileImageUrl;
    private String managerName;
    private String managerEmail;

    private String todoState;   // 현재 상태 기준으로 분기 처리를 위한 데이터

    private String teamName;
    private String teamKey;
    private long todoCreatedTime;
    private long todoEndTime;

    @Embedded
    private ArrayList<Event> eventHistory;  // 이벤트 발생마다의 로그

    @PrimaryKey
    @NonNull
    private String todoKey;

    public TodoData() {
    }


    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDesc() {
        return todoDesc;
    }

    public void setTodoDesc(String todoDesc) {
        this.todoDesc = todoDesc;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getManagerProfileImageUrl() {
        return managerProfileImageUrl;
    }

    public void setManagerProfileImageUrl(String managerProfileImageUrl) {
        this.managerProfileImageUrl = managerProfileImageUrl;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getTodoState() {
        return todoState;
    }

    public void setTodoState(String todoState) {
        this.todoState = todoState;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    public long getTodoCreatedTime() {
        return todoCreatedTime;
    }

    public void setTodoCreatedTime(long todoCreatedTime) {
        this.todoCreatedTime = todoCreatedTime;
    }

    public long getTodoEndTime() {
        return todoEndTime;
    }

    public void setTodoEndTime(long todoEndTime) {
        this.todoEndTime = todoEndTime;
    }

    public ArrayList<Event> getEventHistory() {
        if (eventHistory == null) {
            eventHistory = new ArrayList<>();
        }

        return eventHistory;
    }

    public void setEventHistory(ArrayList<Event> eventHistory) {
        this.eventHistory = eventHistory;
    }

    @NonNull
    public String getTodoKey() {
        return todoKey;
    }

    public void setTodoKey(@NonNull String todoKey) {
        this.todoKey = todoKey;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TodoData) {
            return getTodoKey().equals(((TodoData) obj).getTodoKey());
        } else {
            return false;
        }
    }


    @Override
    public int compareTo(TodoData o) {
        return Long.compare(o.getTodoCreatedTime(), getTodoCreatedTime());
    }
}