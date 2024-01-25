package com.hero.seoultechteams.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public abstract class LocalStore<T> implements DataStore<T> {

    private List<T> dataList = new ArrayList<>();
    private Context context;

    public LocalStore(Context context) {
        this.context = context;
    }

    public LocalStore() {
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Context getContext() {
        return context;
    }

    public void addAll(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        for (T element : dataList) {
            int index = this.dataList.indexOf(element);
            if (index == -1) {
                // create
                this.dataList.add(element);
            } else {
                // update
                this.dataList.set(index, element);
            }
        }
    }
}
