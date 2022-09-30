package com.hero.seoultechteams.database.notice;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.data.notice.remote.NoticeRemoteDataSource;
import com.hero.seoultechteams.data.notice.remote.NoticeRemoteDataSourceImpl;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;
import com.hero.seoultechteams.domain.notice.repository.NoticeRepository;

import java.util.ArrayList;
import java.util.List;

public class NoticeRepositoryImpl implements NoticeRepository {
    private final NoticeRemoteDataSource noticeRemoteDataSource;

    public NoticeRepositoryImpl(NoticeRemoteDataSourceImpl noticeRemoteDataSource) {
        this.noticeRemoteDataSource = noticeRemoteDataSource;
    }

    public void getNotice(final OnCompleteListener<NoticeData> onCompleteListener, final String noticeKey) {
        getNoticeFromCloud(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData noticeData) {
                if (isSuccess && noticeData != null) {
                    onCompleteListener.onComplete(true, noticeData);
                } else {
                    getNoticeFromCloud(onCompleteListener, noticeKey);
                }
            }
        }, noticeKey);
    }

    public void getNoticeList(final OnCompleteListener<List<NoticeEntity>> onCompleteListener) {
        noticeRemoteDataSource.getDataList(new OnCompleteListener<List<NoticeData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<NoticeData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, getNoticeEntities(data));
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        });
    }

    public void getNoticeFromCloud(final OnCompleteListener<NoticeData> onCompleteListener, String noticeKey) {
        noticeRemoteDataSource.getData(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, noticeKey);
    }

    @NonNull
    private List<NoticeEntity> getNoticeEntities(List<NoticeData> data) {
        List<NoticeEntity> result = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).toEntity());
        }
        return result;
    }
}