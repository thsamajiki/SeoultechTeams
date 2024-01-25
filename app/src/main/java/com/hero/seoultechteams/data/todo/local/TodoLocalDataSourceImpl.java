package com.hero.seoultechteams.data.todo.local;

import com.hero.seoultechteams.database.DataType;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.todo.datastore.TodoCacheStore;
import com.hero.seoultechteams.database.todo.datastore.TodoLocalStore;
import com.hero.seoultechteams.database.todo.entity.TodoData;

import java.util.List;

public class TodoLocalDataSourceImpl implements TodoLocalDataSource {
    private final TodoLocalStore todoLocalStore;
    private final TodoCacheStore todoCacheStore;

    public TodoLocalDataSourceImpl(TodoLocalStore todoLocalStore, TodoCacheStore todoCacheStore) {
        this.todoLocalStore = todoLocalStore;
        this.todoCacheStore = todoCacheStore;
    }

    @Override
    public void getData(OnCompleteListener<TodoData> onCompleteListener, String todoKey) {
        todoCacheStore.getData(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    todoLocalStore.getData(new OnCompleteListener<TodoData>() {
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
            }
        }, todoKey);
    }

    @Override
    public void getDataList(OnCompleteListener<List<TodoData>> onCompleteListener, DataType type, String teamKey) {
        todoCacheStore.getDataList(new OnCompleteListener<List<TodoData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<TodoData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    todoLocalStore.getDataList(new OnCompleteListener<List<TodoData>>() {
                        @Override
                        public void onComplete(boolean isSuccess, List<TodoData> data) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, data);
                            } else {
                                onCompleteListener.onComplete(false, null);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void clear() {
        todoCacheStore.clear();
    }

    @Override
    public void add(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        todoLocalStore.add(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData localData) {
                if (isSuccess) {
                    todoCacheStore.add(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData cacheData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, cacheData);
                            } else {
                                onCompleteListener.onComplete(true, localData);
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
    public void update(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        todoLocalStore.update(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData localData) {
                if (isSuccess) {
                    todoCacheStore.update(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData cacheData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, cacheData);
                            } else {
                                onCompleteListener.onComplete(true, localData);
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
    public void remove(OnCompleteListener<TodoData> onCompleteListener, TodoData todoData) {
        todoLocalStore.remove(new OnCompleteListener<TodoData>() {
            @Override
            public void onComplete(boolean isSuccess, TodoData localData) {
                if (isSuccess) {
                    todoCacheStore.remove(new OnCompleteListener<TodoData>() {
                        @Override
                        public void onComplete(boolean isSuccess, TodoData cacheData) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, cacheData);
                            } else {
                                onCompleteListener.onComplete(true, localData);
                            }
                        }
                    }, todoData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, todoData);
    }
}
