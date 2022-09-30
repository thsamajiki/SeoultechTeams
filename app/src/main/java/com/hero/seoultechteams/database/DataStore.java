package com.hero.seoultechteams.database;

import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public interface DataStore<T> {
    void getData(OnCompleteListener<T> onCompleteListener, Object ... params);    // 인자를 0개 이상 넣어도 됨(Null이어도 됨)
    void getDataList(OnCompleteListener<List<T>> onCompleteListener, Object ... params);
    void add(OnCompleteListener<T> onCompleteListener, T data);
    void update(OnCompleteListener<T> onCompleteListener, T data);
    void remove(OnCompleteListener<T> onCompleteListener, T data);
}
