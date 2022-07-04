package com.hero.seoultechteams.database;

public interface OnCompleteListener<T> {
    void onComplete(boolean isSuccess, T data);
}
