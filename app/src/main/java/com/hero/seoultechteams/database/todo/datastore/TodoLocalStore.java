package com.hero.seoultechteams.database.todo.datastore;

import android.content.Context;

import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.LocalDataStore;

import java.util.ArrayList;

public class TodoLocalStore extends LocalDataStore<TodoData> {

    public TodoLocalStore(Context context) {
        super(context);
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, Object... params) {

    }

    @Override
    public void getDataList(OnCompleteListener<ArrayList<TodoData>> onCompleteListener, Object... params) {

    }

    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {

    }

    @Override
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {

    }

    @Override
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {

    }


}