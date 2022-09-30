package com.hero.seoultechteams.view.main.account.setting.notice.contract;

import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class NoticeListContract {
    public interface View {
        void onGetNoticeList(List<NoticeEntity> noticeDataList);

        void failedGetNoticeList();
    }

    public interface Presenter {
        void getNoticeListFromDatabase(List<NoticeEntity> noticeDataList);
    }
}
