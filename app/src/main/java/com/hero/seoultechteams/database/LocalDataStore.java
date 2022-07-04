package com.hero.seoultechteams.database;

import android.content.Context;

import java.util.ArrayList;


public abstract class LocalDataStore<T> implements DataStore<T> {

    private ArrayList<T> dataList = new ArrayList<>();
    private Context context;
    private AppDataBase appDataBase;

    public LocalDataStore(Context context) {
        this.context = context;
        this.appDataBase = AppDataBase.getInstance(context);
    }

    public LocalDataStore() {
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public AppDataBase getAppDataBase() {
        return appDataBase;
    }

    public Context getContext() {
        return context;
    }

    public void addAll(ArrayList<T> dataList) {
        if (dataList == null) {
            return;
        }
        for (T element : dataList) {
            int index = this.dataList.indexOf(element);
            if (index != -1) {
                this.dataList.add(element);
            } else {
                this.dataList.set(index, element);
            }
        }
    }
}