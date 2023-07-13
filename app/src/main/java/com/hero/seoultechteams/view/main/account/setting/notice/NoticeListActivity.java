package com.hero.seoultechteams.view.main.account.setting.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hero.seoultechteams.BaseActivity;
import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.databinding.ActivityNoticeListBinding;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.account.setting.notice.contract.NoticeListContract;
import com.hero.seoultechteams.view.main.account.setting.notice.presenter.NoticeListPresenter;

import java.util.ArrayList;
import java.util.List;


public class NoticeListActivity extends BaseActivity<ActivityNoticeListBinding> implements View.OnClickListener, OnRecyclerItemClickListener<NoticeEntity>, NoticeListContract.View {

    private List<NoticeEntity> noticeDataList = new ArrayList<>();
    private NoticeListAdapter noticeListAdapter;
    private final NoticeListContract.Presenter presenter = new NoticeListPresenter(this,
            Injector.getInstance().provideGetNoticeListUseCase());

    @Override
    protected ActivityNoticeListBinding getViewBinding() {
        return ActivityNoticeListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNoticeListRecyclerViewAdapter();
        presenter.getNoticeListFromDatabase(noticeDataList);
        setOnClickListener();
    }


    private void initNoticeListRecyclerViewAdapter() {
        noticeListAdapter = new NoticeListAdapter(this, noticeDataList);
        noticeListAdapter.setOnRecyclerItemClickListener(this);
        binding.rvNoticeList.setAdapter(noticeListAdapter);
    }

    private void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(int position, View view, NoticeEntity data) {

    }

    @Override
    public void onGetNoticeList(List<NoticeEntity> data) {
        noticeDataList.clear();
        noticeDataList.addAll(data);
        noticeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void failedGetNoticeList() {
        Toast.makeText(this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}