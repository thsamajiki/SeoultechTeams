package com.hero.seoultechteams.database.todo.datastore;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class TodoCacheStore extends CacheStore<TodoData> {

    private static TodoCacheStore instance;

    private TodoCacheStore() {
    }

    public static TodoCacheStore getInstance() {
        if (instance == null) {
            instance = new TodoCacheStore();
        }
        return instance;
    }


    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, Object... params) {
        if (onCompleteListener == null) {
            return;
        }

        String todoKey = params[0].toString();

        for (TodoData todoData : getDataList()) {
            if (todoData.getTeamKey().equals(todoKey)) {
                onCompleteListener.onComplete(true, todoData);
                return;
            }
        }

        onCompleteListener.onComplete(false, null);
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, Object... params) {
        if (onCompleteListener == null) {
            return;
        }

        if (params.length > 0) {
            DataType type = (DataType) params[0];
            String key = params[1].toString();

            switch (type) {
                case MY:
                    getMyTodoList(onCompleteListener, key);
                    break;

                case TEAM:
                    getTeamTodoList(onCompleteListener, key);
                    break;

                default:
                    throw new IllegalArgumentException("정의되지 않은 타입입니다.");
            }
        } else {
            onCompleteListener.onComplete(false, null);
        }
    }

    private void getMyTodoList(OnCompleteListener<List<TodoData>> onCompleteListener, String userKey) {
        List<TodoData> todoDataList = getDataList();
        if (todoDataList.isEmpty()) {
            onCompleteListener.onComplete(true, null);
        } else {
            List<TodoData> myTodoList = new ArrayList<>();
            for (TodoData todoData : todoDataList) {
                if (todoData.getUserKey().equals(userKey)) {
                    myTodoList.add(todoData);
                }
            }
            if (myTodoList.isEmpty()) {
                onCompleteListener.onComplete(true, null);
            } else {
                onCompleteListener.onComplete(true, myTodoList);
            }
        }
    }

    private void getTeamTodoList(OnCompleteListener<List<TodoData>> onCompleteListener, String teamKey) {
        List<TodoData> todoDataList = getDataList();
        if (todoDataList.isEmpty()) {
            onCompleteListener.onComplete(true, null);
        } else {
            List<TodoData> teamTodoList = new ArrayList<>();
            for (TodoData todoData : todoDataList) {
                if (todoData.getTeamKey().equals(teamKey)) {
                    teamTodoList.add(todoData);
                }
            }
            if (teamTodoList.isEmpty()) {
                onCompleteListener.onComplete(true, null);
            } else {
                onCompleteListener.onComplete(true, teamTodoList);
            }
        }
    }


    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        getDataList().add(data);
        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data);
        }
    }

    @Override
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex != -1) {
            getDataList().set(originIndex, data);
        }

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true, data);
        }
    }

    @Override
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        int originIndex = getDataList().indexOf(data);
        if (originIndex == -1) {
            throw new IndexOutOfBoundsException("기존 데이터가 없습니다.");
        } else {
            getDataList().remove(originIndex);
        }
    }
}
