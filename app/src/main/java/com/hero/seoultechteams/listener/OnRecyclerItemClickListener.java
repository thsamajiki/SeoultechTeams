package com.hero.seoultechteams.listener;

import android.view.View;

public interface OnRecyclerItemClickListener<T> {
    void onItemClick(int position, View view, T data);
}
