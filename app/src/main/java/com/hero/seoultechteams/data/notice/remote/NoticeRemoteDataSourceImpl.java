package com.hero.seoultechteams.data.notice.remote;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.notice.datastore.NoticeCloudStore;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

import java.util.ArrayList;
import java.util.List;

public class NoticeRemoteDataSourceImpl implements NoticeRemoteDataSource {
    private final NoticeCloudStore noticeCloudStore;

    public NoticeRemoteDataSourceImpl(NoticeCloudStore noticeCloudStore) {
        this.noticeCloudStore = noticeCloudStore;
    }

    @Override
    public void getData(OnCompleteListener<NoticeData> onCompleteListener, String noticeKey) {
        noticeCloudStore.getData(onCompleteListener, noticeKey);
    }

    @Override
    public void getDataList(OnCompleteListener<List<NoticeData>> onCompleteListener) {
        noticeCloudStore.getDataList(onCompleteListener);
    }
}
