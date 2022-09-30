package com.hero.seoultechteams.data.todo.local;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.ArrayList;
import java.util.List;

public interface TodoLocalDataSource {
    // CacheStore
    void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, DataType type, String teamKey);

    void clear();

    // LocalStore
    void add(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    void update(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);
}