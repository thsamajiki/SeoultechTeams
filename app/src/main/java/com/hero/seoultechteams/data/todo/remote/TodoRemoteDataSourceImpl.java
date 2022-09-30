package com.hero.seoultechteams.data.todo.remote;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.datastore.TodoCloudStore;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.ArrayList;
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
    public void getDataList(OnCompleteListener<List<TodoData>> arrayListOnCompleteListener, DataType team, String teamKey) {
        todoCloudStore.getDataList(arrayListOnCompleteListener, team, teamKey);
    }
}