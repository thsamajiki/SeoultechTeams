package com.hero.seoultechteams.data.todo.local;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public interface TodoLocalDataSource {
    // CacheStore
    void getData(OnCompleteListener<TodoData> onCompleteListener, String todoKey);

    void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, DataType type, String key);

    void clear();

    // LocalStore
    void add(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    void update(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);

    void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData);
}
