package com.hero.seoultechteams.database.todo.datastore;

import android.content.Context;

import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.todo.dao.TodoDao;
import com.hero.seoultechteams.database.todo.database.AppTodoDatabase;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.utils.AppExecutors;

import java.util.List;

public class TodoLocalStore extends LocalStore<TodoData> {

    private AppTodoDatabase appTodoDatabase;
    private TodoDao todoDao;
    private static TodoLocalStore instance;
    private final AppExecutors appExecutors = new AppExecutors();

    private TodoLocalStore(Context context, TodoDao todoDao) {
        super(context);
        this.todoDao = todoDao;
    }

    private TodoLocalStore() {
    }

    public static TodoLocalStore getInstance(Context context, TodoDao todoDao) {
        if (instance == null) {
            instance = new TodoLocalStore(context, todoDao);
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String todoKey = params[0].toString();
//                TodoData todoData = getTodoDatabase().getTodoDao().getTodoFromKey(todoKey);
                TodoData todoData = todoDao.getTodoFromKey(todoKey);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, todoData);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
//                List<TodoData> todoDataList = getTodoDatabase().getTodoDao().getAllTodos();
                List<TodoData> todoDataList = todoDao.getAllTodos();
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, todoDataList);
                        }
                    }
                });
            }
        });
    }

//    private AppTodoDatabase getTodoDatabase() {
//        return appTodoDatabase;
//    }

    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
//                getTodoDatabase().getTodoDao().insertData(data);
                todoDao.insertData(data);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, data);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
//                getTodoDatabase().getTodoDao().updateData(data);
                todoDao.updateData(data);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, data);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData data) {
        if (data != null) {
            appExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
//                    getTodoDatabase().getTodoDao().deleteData(data);
                    todoDao.deleteData(data);
                    // 위에 까지는 서브 스레드에서

                    // onCompleteListener는 메인 스레드에서
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (onCompleteListener != null) {
                                onCompleteListener.onComplete(true, data);
                            }
                        }
                    });
                }
            });
        }
    }
}