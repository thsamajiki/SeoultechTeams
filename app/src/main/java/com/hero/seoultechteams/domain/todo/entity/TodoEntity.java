package com.hero.seoultechteams.domain.todo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.room.Embedded;

import com.hero.seoultechteams.database.todo.entity.Event;

import java.util.ArrayList;
import java.util.List;

public class TodoEntity implements Parcelable, Comparable<TodoEntity> {
    public static final String TODO_STATE_IN_PROGRESS = "inProgress";
    public static final String TODO_STATE_DISMISSED = "dismissed";
    public static final String TODO_STATE_CONFIRMED = "confirmed";
    public static final String TODO_STATE_SUBMITTED = "submitted";

    @StringDef({TODO_STATE_IN_PROGRESS, TODO_STATE_DISMISSED, TODO_STATE_CONFIRMED, TODO_STATE_SUBMITTED})
    public @interface TodoState{}

    public TodoEntity(String todoTitle, String todoDesc, String userKey, String managerProfileImageUrl,
                      String managerName, String managerEmail, String todoState, String teamName, String teamKey,
                      long todoCreatedTime, long todoEndTime, List<Event> eventHistory, String todoKey) {
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

    private String todoTitle;
    private String todoDesc;
    private String userKey;
    private String managerProfileImageUrl;
    private String managerName;
    private String managerEmail;

    @TodoState
    private String todoState;   // 현재 상태 기준으로 분기 처리를 위한 데이터

    private String teamName;
    private String teamKey;
    private long todoCreatedTime;
    private long todoEndTime;

    @Embedded
    private List<Event> eventHistory = new ArrayList<>();  // 이벤트 발생마다의 로그

    private String todoKey;

    protected TodoEntity(Parcel in) {
        this.todoTitle = in.readString();
        this.todoDesc = in.readString();
        this.userKey = in.readString();
        this.managerProfileImageUrl = in.readString();
        this.managerName = in.readString();
        this.managerEmail = in.readString();
        this.todoState = in.readString();
        this.teamName = in.readString();
        this.teamKey = in.readString();
        this.todoCreatedTime = in.readLong();
        this.todoEndTime = in.readLong();
        this.eventHistory = new ArrayList<>();
        in.readTypedList(this.eventHistory, Event.CREATOR);
        this.todoKey = in.readString();
    }

    public static final Creator<TodoEntity> CREATOR = new Creator<TodoEntity>() {
        @Override
        public TodoEntity createFromParcel(Parcel in) {
            return new TodoEntity(in);
        }

        @Override
        public TodoEntity[] newArray(int size) {
            return new TodoEntity[size];
        }
    };

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

    public List<Event> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<Event> eventHistory) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.todoTitle);
        dest.writeString(this.todoDesc);
        dest.writeString(this.userKey);
        dest.writeString(this.managerProfileImageUrl);
        dest.writeString(this.managerName);
        dest.writeString(this.managerEmail);
        dest.writeString(this.todoState);
        dest.writeString(this.teamName);
        dest.writeString(this.teamKey);
        dest.writeLong(this.todoCreatedTime);
        dest.writeLong(this.todoEndTime);
        dest.writeTypedList(this.eventHistory);
        dest.writeString(this.todoKey);
    }

    public void readFromParcel(Parcel source) {
        this.todoTitle = source.readString();
        this.todoDesc = source.readString();
        this.userKey = source.readString();
        this.managerProfileImageUrl = source.readString();
        this.managerName = source.readString();
        this.managerEmail = source.readString();
        this.todoState = source.readString();
        this.teamName = source.readString();
        this.teamKey = source.readString();
        this.todoCreatedTime = source.readLong();
        this.todoEndTime = source.readLong();
        this.eventHistory = new ArrayList<>();
        source.readTypedList(this.eventHistory,Event.CREATOR);
        this.todoKey = source.readString();
    }

    @Override
    public int compareTo(TodoEntity o) {
        return Long.compare(o.getTodoCreatedTime(), getTodoCreatedTime());
    }
}
