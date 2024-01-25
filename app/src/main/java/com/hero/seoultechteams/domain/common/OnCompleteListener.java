package com.hero.seoultechteams.domain.common;

public interface OnCompleteListener<T> {
    void onComplete(boolean isSuccess, T data);
}
