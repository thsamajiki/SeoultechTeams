package com.hero.seoultechteams.database;

import java.util.ArrayList;
import java.util.List;

public abstract class CacheStore<T> implements DataStore<T> {

    private List<T> dataList = new ArrayList<>();

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public void add(T data) {
        if(!this.dataList.contains(data)) {
            this.dataList.add(data);
        }
    }

    public void add(int index, T data) {
        if(!this.dataList.contains(data)) {
            this.dataList.add(index, data);
        }
    }

    public void addAll(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        for (T element : dataList) {
            int index = this.dataList.indexOf(element);
            if (index != -1) {
                this.dataList.set(index, element);
            } else {
                this.dataList.add(element);
            }
        }
    }

    public void clear() {
        this.dataList.clear();
    }
}
