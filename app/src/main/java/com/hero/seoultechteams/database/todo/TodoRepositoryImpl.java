package com.hero.seoultechteams.database.todo;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.data.todo.local.TodoLocalDataSource;
import com.hero.seoultechteams.data.todo.remote.TodoRemoteDataSource;
import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.domain.todo.entity.TodoEntity;
import com.hero.seoultechteams.domain.todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;


/*
TodoListFragment -> Presenter -> UseCase -> Repository -> DataStore(Remote)
*/

public class TodoRepositoryImpl implements TodoRepository {

    private boolean refresh = false;

    private final TodoRemoteDataSource todoRemoteDataSource;
    private final TodoLocalDataSource todoLocalDataSource;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public TodoRepositoryImpl(TodoRemoteDataSource todoRemoteDataSource, TodoLocalDataSource todoLocalDataSource) {
        this.todoRemoteDataSource = todoRemoteDataSource;
        this.todoLocalDataSource = todoLocalDataSource;
    }

    public void addTodo(OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        TodoData todoData = TodoData.toData(todoEntity);

        todoLocalDataSource.add(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, todoData);

        todoRemoteDataSource.add(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, todoData);
    }

    public void updateTodo(OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        TodoData todoData = TodoData.toData(todoEntity);

        todoLocalDataSource.update(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, todoData);

        todoRemoteDataSource.update(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, todoData);
    }

    public void removeTodo(OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        TodoData todoData = TodoData.toData(todoEntity);

        todoLocalDataSource.remove(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, todoData);

        todoRemoteDataSource.remove(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                onCompleteListener.onComplete(isSuccess, data.toEntity());
            }
        }, todoData);
    }

    public void getTeamTodoList(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String teamKey) {
        getTeamTodoListFromCloud(onCompleteListener, teamKey);
        getTeamTodoListFromCache(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess) {
//                    onCompleteListener.onComplete(isSuccess, data);
                } else {
//                    getTeamTodoListFromLocal(onCompleteListener, teamKey);
                    getTeamTodoListFromCloud(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    @Override
    public void getMyTodoList(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String userKey) {
        if (isRefresh()) {
            todoLocalDataSource.clear();
            getMyTodoListFromCloud(onCompleteListener, userKey);
            setRefresh(false);
            return;
        }
        getMyTodoListFromCache(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getTodoEntities(data));
                } else {
                    getMyTodoListFromLocal(onCompleteListener, userKey);
                    getMyTodoListFromCloud(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    private void getTeamTodoListFromCache(final OnCompleteListener<List<TodoData>> onCompleteListener,
                                          final String teamKey) {
        todoLocalDataSource.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, DataType.TEAM, teamKey);
    }

    private void getMyTodoListFromCache(final OnCompleteListener<List<TodoData>> onCompleteListener,
                                        final String userKey) {
        todoLocalDataSource.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, DataType.MY, userKey);
    }

    private void getTeamTodoListFromLocal(final OnCompleteListener<List<TodoData>> onCompleteListener, final String teamKey) {
        todoLocalDataSource.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> todoData) {
                if (isSuccess && todoData != null) {
                    onCompleteListener.onComplete(true, todoData);
                } else {
//                    getTeamTodoListFromCloud(onCompleteListener, teamKey);
                }
            }
        }, DataType.TEAM, teamKey);
    }

    private void getMyTodoListFromLocal(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String userKey) {
        todoLocalDataSource.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess && data != null) {
//                    onCompleteListener.onComplete(true, data);
                } else {
                    getMyTodoListFromCloud(onCompleteListener, userKey);
                }
            }
        }, DataType.MY, userKey);
    }

    private void getTeamTodoListFromCloud(final OnCompleteListener<List<TodoEntity>> onCompleteListener, String teamKey) {
        todoRemoteDataSource.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, getTodoEntities(data));
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }

        }, DataType.TEAM, teamKey);
    }

    private void getMyTodoListFromCloud(final OnCompleteListener<List<TodoEntity>> onCompleteListener, String userKey) {
        todoRemoteDataSource.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, getTodoEntities(data));
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, DataType.MY, userKey);
    }

//    private void getMyEmptyTeamTodoFromCloud(final OnCompleteListener<ArrayList<TodoEntity>> onCompleteListener,
//                                             String userKey,
//                                             ArrayList<String> teamKeyList) {
//        todoRemoteDataSource.getDataList(new OnCompleteListener<ArrayList<TodoData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<TodoData> data) {
//                if (isSuccess) {
//                    onCompleteListener.onComplete(true, getTodoEntities(data));
//                } else {
//                    onCompleteListener.onComplete(false, null);
//                }
//            }
//        }, DataType.MY, userKey, teamKeyList);
//    }

    @NonNull
    private List<TodoEntity> getTodoEntities(List<TodoData> data) {
        List<TodoEntity> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).toEntity());
        }
        return result;
    }
}