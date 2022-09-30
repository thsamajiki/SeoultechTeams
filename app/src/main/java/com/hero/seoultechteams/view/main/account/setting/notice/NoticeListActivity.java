package com.hero.seoultechteams.view.main.account.setting.notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hero.seoultechteams.Injector;
import com.hero.seoultechteams.R;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;
import com.hero.seoultechteams.listener.OnRecyclerItemClickListener;
import com.hero.seoultechteams.view.main.account.setting.notice.contract.NoticeListContract;
import com.hero.seoultechteams.view.main.account.setting.notice.presenter.NoticeListPresenter;

import java.util.ArrayList;
import java.util.List;


public class NoticeListActivity extends AppCompatActivity implements View.OnClickListener, OnRecyclerItemClickListener<NoticeEntity>, NoticeListContract.View {

    private ImageView btnBack, ivToggleArrow;
    private LinearLayout llContentNoticeItem;
    private List<NoticeEntity> noticeDataList = new ArrayList<>();
    private RecyclerView rvNoticeList;
    private NoticeListAdapter noticeListAdapter;
    private final NoticeListContract.Presenter presenter = new NoticeListPresenter(this,
            Injector.getInstance().provideGetNoticeListUseCase());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        initView();
        initNoticeListRecyclerViewAdapter();
        presenter.getNoticeListFromDatabase(noticeDataList);
        setOnClickListener();
    }

    private void initView() {
        btnBack = findViewById(R.id.iv_back);
        ivToggleArrow = findViewById(R.id.iv_toggle_arrow);
        llContentNoticeItem = findViewById(R.id.ll_content_notice_item);
        rvNoticeList = findViewById(R.id.rv_notice_list);
    }

    private void initNoticeListRecyclerViewAdapter() {
        noticeListAdapter = new NoticeListAdapter(this, noticeDataList);
        noticeListAdapter.setOnRecyclerItemClickListener(this);
        rvNoticeList.setAdapter(noticeListAdapter);
    }

//    private void getNoticeListFromDatabase() {
//        NoticeRepositoryImpl noticeRepository = new NoticeRepositoryImpl(this);
//        noticeRepository.getNoticeList(new OnCompleteListener<ArrayList<NoticeData>>() {
//            @Override
//            public void onComplete(boolean isSuccess, ArrayList<NoticeData> data) {
//                if (isSuccess && data != null) {
//                    noticeDataList.addAll(data);
//                    noticeListAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(NoticeListActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private void setOnClickListener() {
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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