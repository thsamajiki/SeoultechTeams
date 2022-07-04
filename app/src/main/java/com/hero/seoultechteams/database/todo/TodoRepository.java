package com.hero.seoultechteams.database.todo;

import android.content.Context;
import android.util.Log;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.Repository;
import com.hero.seoultechteams.database.todo.datastore.TodoCacheStore;
import com.hero.seoultechteams.database.todo.datastore.TodoCloudStore;
import com.hero.seoultechteams.database.todo.datastore.TodoLocalStore;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.LocalDataStore;

import java.util.ArrayList;


public class TodoRepository extends Repository<TodoData> {

    private boolean refresh = false;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public TodoRepository(Context context) {
        super(context);
    }

    @Override
    protected CloudStore<TodoData> createCloudStore(Context context) {
        return new TodoCloudStore(context);
    }

    @Override
    protected LocalDataStore<TodoData> createLocalStore(Context context) {
        return new TodoLocalStore(context);
    }

    @Override
    protected CacheStore<TodoData> createCacheStore() {
        return TodoCacheStore.getInstance();
    }

    public void addTodo(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        getCloudStore().add(onCompleteListener, todoData);
    }

    public void updateTodo(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        getCloudStore().update(onCompleteListener, todoData);
    }

    public void removeTodo(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        getCloudStore().remove(onCompleteListener, todoData);
    }

    public void getTeamTodoList(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener, final String teamKey) {
        getTeamTodoFromCloud(onCompleteListener, teamKey);
//        getTeamTodoFromCache(new OnCompleteListener<ArrayList<TodoData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
//                if (isSuccess && data != null) {
//                    onCompleteListener.onComplete(true, data);
//                } else {
//                    getTeamTodoFromCloud(onCompleteListener, teamKey);
//                }
//            }
//        }, teamKey);
    }

    public void getMyTodoList(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener, final String userKey) {
        if (isRefresh()) {
            getCacheStore().clear();
            getMyTodoFromCloud(onCompleteListener, userKey);
            setRefresh(false);
            return;
        }
        getMyTodoFromCache(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    getMyTodoFromCloud(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    private void getTeamTodoFromCache(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener,
                                      final String teamKey) {
        getCacheStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, DataType.TEAM, teamKey);
    }

    private void getMyTodoFromCache(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener,
                                    final String userKey) {
        getCacheStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, DataType.MY, userKey);
    }

    private void getTeamTodoFromLocal(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener, final String teamKey) {
        getLocalStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> todoData) {
                if (isSuccess && isNotEmptyList(todoData)) {
                    onCompleteListener.onComplete(true, todoData);
                } else {
                    getTeamTodoFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    private void getMyTodoFromLocal(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener, final String userKey) {
        getLocalStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> todoData) {
                if (isSuccess && isNotEmptyList(todoData)) {
                    onCompleteListener.onComplete(true, todoData);
                } else {
                    getMyTodoFromCloud(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    private void getTeamTodoFromCloud(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener, String teamKey) {
        getCloudStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                if (isSuccess) {

                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, DataType.TEAM, teamKey);
    }

    private void getMyTodoFromCloud(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener, String userKey) {
        getCloudStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, DataType.MY, userKey);
    }

    private void getMyEmptyTeamTodoFromCloud(final OnCompleteListener<ArrayList<TodoData>> onCompleteListener,
                                             String userKey,
                                             ArrayList<String> teamKeyList) {
        getCloudStore().getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, ArrayList<TodoData> todoData) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, todoData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, DataType.MY, userKey, teamKeyList);
    }
}