package com.hero.seoultechteams.view.main.account.setting.notice.presenter;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;
import com.hero.seoultechteams.domain.notice.usecase.GetNoticeListUseCase;
import com.hero.seoultechteams.view.main.account.setting.notice.contract.NoticeListContract;

import java.util.ArrayList;
import java.util.List;

public class NoticeListPresenter implements NoticeListContract.Presenter {
    private final NoticeListContract.View view;
    private final GetNoticeListUseCase getNoticeListUseCase;

    public NoticeListPresenter(NoticeListContract.View view, GetNoticeListUseCase getNoticeListUseCase) {
        this.view = view;
        this.getNoticeListUseCase = getNoticeListUseCase;
    }

    @Override
    public void getNoticeListFromDatabase(List<NoticeEntity> noticeDataList) {
        getNoticeListUseCase.invoke(new OnCompleteListener<List<NoticeEntity>>() {
            @Override
            public void onComplete(boolean isSuccess, List<NoticeEntity> data) {
                if (isSuccess && data != null) {
                    view.onGetNoticeList(data);
                } else {
                    view.failedGetNoticeList();
                }
            }
        });
    }
}
