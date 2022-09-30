package com.hero.seoultechteams.data.todo.local;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.datastore.TodoCacheStore;
import com.hero.seoultechteams.database.todo.datastore.TodoLocalStore;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.ArrayList;
import java.util.List;

public class TodoLocalDataSourceImpl implements TodoLocalDataSource {
    private final TodoLocalStore todoLocalStore;
    private final TodoCacheStore todoCacheStore;

    public TodoLocalDataSourceImpl(TodoLocalStore todoLocalStore, TodoCacheStore todoCacheStore) {
        this.todoLocalStore = todoLocalStore;
        this.todoCacheStore = todoCacheStore;
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, DataType type, String teamKey) {
        todoCacheStore.getDataList(onCompleteListener, type, teamKey);

        todoCacheStore.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    todoLocalStore.getDataList(new OnCompleteListener<List<TodoData>>() {
                        @Override
                        public void onComplete(boolean isSuccess, List<TodoData> data) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, data);
                            } else {

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void clear() {
        todoCacheStore.clear();
    }

    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        todoLocalStore.add(onCompleteListener, todoData);
    }

    @Override
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        todoLocalStore.update(onCompleteListener, todoData);
    }

    @Override
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        todoLocalStore.remove(onCompleteListener, todoData);
    }
}