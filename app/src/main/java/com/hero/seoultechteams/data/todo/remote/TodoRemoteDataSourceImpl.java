package com.hero.seoultechteams.data.todo.remote;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.todo.datastore.TodoCloudStore;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public class TodoRemoteDataSourceImpl implements TodoRemoteDataSource {

    private final TodoCloudStore todoCloudStore;

    public TodoRemoteDataSourceImpl(TodoCloudStore todoCloudStore) {
        this.todoCloudStore = todoCloudStore;
    }

    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData todoEntity) {
        todoCloudStore.add(onCompleteListener, todoEntity);
    }

    @Override
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData todoEntity) {
        todoCloudStore.update(onCompleteListener, todoEntity);
    }

    @Override
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData todoEntity) {
        todoCloudStore.remove(onCompleteListener, todoEntity);
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, String todoKey) {
        todoCloudStore.getData(onCompleteListener, todoKey);
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, DataType type, String key) {
        todoCloudStore.getDataList(onCompleteListener, type, key);
    }
}