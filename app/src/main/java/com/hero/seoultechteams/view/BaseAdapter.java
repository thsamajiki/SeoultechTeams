package com.hero.seoultechteams.view;

import androidx.recyclerview.widget.RecyclerView;

import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;


public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, D> extends RecyclerView.Adapter<T> {

    private OnRecyclerItemClickListener<D> onRecyclerItemClickListener;

    public OnRecyclerItemClickListener<D> getOnRecyclerItemClickListener() {
        return onRecyclerItemClickListener;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<D> onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }
}
