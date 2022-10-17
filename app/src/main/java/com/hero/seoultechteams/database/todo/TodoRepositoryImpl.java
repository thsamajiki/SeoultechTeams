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

        todoRemoteDataSource.add(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData remoteData) {
                if (isSuccess) {
                    todoLocalDataSource.add(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData localData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity());
                            } else {
                                onCompleteListener.onComplete(true, remoteData.toEntity());
                            }
                        }
                    }, todoData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, todoData);
    }

    public void updateTodo(OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        TodoData todoData = TodoData.toData(todoEntity);

        todoRemoteDataSource.update(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData remoteData) {
                if (isSuccess) {
                    todoLocalDataSource.update(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData localData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity());
                            } else {
                                onCompleteListener.onComplete(true, remoteData.toEntity());
                            }
                        }
                    }, todoData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, todoData);
    }

    public void removeTodo(OnCompleteListener<TodoEntity> onCompleteListener, TodoEntity todoEntity) {
        TodoData todoData = TodoData.toData(todoEntity);

        todoRemoteDataSource.remove(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData remoteData) {
                if (isSuccess) {
                    todoLocalDataSource.remove(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData localData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, localData.toEntity());
                            } else {
                                onCompleteListener.onComplete(true, remoteData.toEntity());
                            }
                        }
                    }, todoData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, todoData);
    }

    @Override
    public void getTodo(OnCompleteListener<TodoEntity> onCompleteListener, String todoKey) {
        getTodoFromLocal(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData localData) {
                if (isSuccess && localData != null) {
                    onCompleteListener.onComplete(true, localData.toEntity());
                } else {
                    getTodoFromRemote(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData remoteData) {
                            TodoEntity data = null;
                            if (remoteData != null) {
                                data = remoteData.toEntity();
                            }
                            onCompleteListener.onComplete(isSuccess, data);
                        }
                    }, todoKey);
                }
            }
        }, todoKey);
    }

    public void getTeamTodoList(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String teamKey) {
        getTeamTodoListFromLocal(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getTodoEntities(data));
                } else {
                    getTeamTodoListFromRemote(onCompleteListener, teamKey);
                }
            }
        }, teamKey);
    }

    @Override
    public void getMyTodoList(final OnCompleteListener<List<TodoEntity>> onCompleteListener, final String userKey) {
        if (isRefresh()) {
            todoLocalDataSource.clear();
            getMyTodoListFromRemote(onCompleteListener, userKey);
            setRefresh(false);
            return;
        }

        getMyTodoListFromLocal(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess && data != null) {
                    onCompleteListener.onComplete(true, getTodoEntities(data));
                } else {
                    getMyTodoListFromRemote(onCompleteListener, userKey);
                }
            }
        }, userKey);
    }

    private void getTodoFromLocal(final OnCompleteListener<TodoData> onCompleteListener, final String todoKey) {
        todoLocalDataSource.getData(onCompleteListener, todoKey);
    }

    private void getTodoFromRemote(final OnCompleteListener<TodoData> onCompleteListener, final String todoKey) {
        todoRemoteDataSource.getData(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, todoKey);
    }

    private void getTeamTodoListFromLocal(final OnCompleteListener<List<TodoData>> onCompleteListener, final String teamKey) {
        todoLocalDataSource.getDataList(onCompleteListener, DataType.TEAM, teamKey);
    }

    private void getTeamTodoListFromRemote(final OnCompleteListener<List<TodoEntity>> onCompleteListener, String teamKey) {
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

    private void getMyTodoListFromLocal(final OnCompleteListener<List<TodoData>> onCompleteListener, final String userKey) {
        todoLocalDataSource.getDataList(onCompleteListener, DataType.MY, userKey);
    }

    private void getMyTodoListFromRemote(final OnCompleteListener<List<TodoEntity>> onCompleteListener, String userKey) {
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

    @NonNull
    private List<TodoEntity> getTodoEntities(List<TodoData> data) {
        List<TodoEntity> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).toEntity());
        }
        return result;
    }
}