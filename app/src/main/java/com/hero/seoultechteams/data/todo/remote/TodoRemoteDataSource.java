package com.hero.seoultechteams.data.todo.remote;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public interface TodoRemoteDataSource {

    // Create
    void add(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    // Update
    void update(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    // Delete
    void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    // Read
    void getData(OnCompleteListener<TodoData> onCompleteListener, String todoKey);

    void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, DataType type, String key);
}