package com.hero.seoultechteams.database;

import android.content.Context;

import java.util.ArrayList;

public abstract class Repository<T> {

    private CloudStore<T> cloudStore;
    private LocalStore<T> localStore;
    private CacheStore<T> cacheStore;
    private Context context;

    public Repository(Context context) {
        this.context = context;
        cloudStore = createCloudStore(context);
        localStore = createLocalStore(context);
        cacheStore = createCacheStore();
    }

    public Context getContext() {
        return context;
    }

    protected abstract CloudStore<T> createCloudStore(Context context);
    protected abstract LocalStore<T> createLocalStore(Context context);
    protected abstract CacheStore<T> createCacheStore();

    protected CloudStore<T> getCloudStore() {
        return cloudStore;
    }
    protected LocalStore<T> getLocalStore() {
        return localStore;
    }
    protected CacheStore<T> getCacheStore() {
        return cacheStore;
    }

    public boolean isEmpty(T data) {
        return data == null;
    }
    public boolean isNotEmpty(T data) {
        return data != null;
    }
    public boolean isEmptyList(ArrayList<T> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isNotEmptyList(ArrayList<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
