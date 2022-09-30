package com.hero.seoultechteams.database.todo.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.todo.database.AppTodoDatabase;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.LocalStore;

import java.util.List;

public class TodoLocalStore extends LocalStore<TodoData> {

    private AppTodoDatabase appTodoDatabase;
    private static TodoLocalStore instance;

    public TodoLocalStore(Context context, AppTodoDatabase appTodoDatabase) {
        super(context);
        this.appTodoDatabase = appTodoDatabase;
    }

    private TodoLocalStore() {
    }

    public static TodoLocalStore getInstance() {
        if (instance == null) {
            instance = new TodoLocalStore();
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String todoKey = params[0].toString();
                TodoData todoData = getTodoDatabase().getTodoDao().getTodoFromKey(todoKey);
                onCompleteListener.onComplete(true, todoData);
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        List<TodoData> todoDataList = getTodoDatabase().getTodoDao().getAllTodos();
        onCompleteListener.onComplete(true, todoDataList);
    }

    public AppTodoDatabase getTodoDatabase() {
        return appTodoDatabase;
    }

    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        getTodoDatabase().getTodoDao().insertData(data);
    }

    @Override
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        getTodoDatabase().getTodoDao().updateData(data);
    }

    @Override
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        if (data != null) {
            getTodoDatabase().getTodoDao().deleteData(data);
        }
    }
}