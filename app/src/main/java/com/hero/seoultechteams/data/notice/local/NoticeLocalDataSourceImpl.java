package com.hero.seoultechteams.data.notice.local;

import com.hero.seoultechteams.database.notice.datastore.NoticeLocalStore;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public class NoticeLocalDataSourceImpl implements NoticeLocalDataSource {
    private final NoticeLocalStore noticeLocalStore;

    public NoticeLocalDataSourceImpl(NoticeLocalStore noticeLocalStore) {
        this.noticeLocalStore = noticeLocalStore;
    }

    @Override
    public void getData(OnCompleteListener<NoticeData> onCompleteListener, String noticeKey) {
        noticeLocalStore.getData(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<NoticeData>> onCompleteListener) {
        noticeLocalStore.getDataList(new OnCompleteListener<List<NoticeData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<NoticeData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        });
    }
}
