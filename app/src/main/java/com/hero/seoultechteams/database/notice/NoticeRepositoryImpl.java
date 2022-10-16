package com.hero.seoultechteams.database.notice;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.data.notice.local.NoticeLocalDataSource;
import com.hero.seoultechteams.data.notice.remote.NoticeRemoteDataSource;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;
import com.hero.seoultechteams.domain.notice.repository.NoticeRepository;

import java.util.ArrayList;
import java.util.List;

public class NoticeRepositoryImpl implements NoticeRepository {
    private final NoticeRemoteDataSource noticeRemoteDataSource;
    private final NoticeLocalDataSource noticeLocalDataSource;

    public NoticeRepositoryImpl(NoticeRemoteDataSource noticeRemoteDataSource, NoticeLocalDataSource noticeLocalDataSource) {
        this.noticeRemoteDataSource = noticeRemoteDataSource;
        this.noticeLocalDataSource = noticeLocalDataSource;
    }

    public void getNotice(final OnCompleteListener<NoticeEntity> onCompleteListener, final String noticeKey) {
        getNoticeFromLocal(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData localData) {
                if (isSuccess && localData != null) {
                    onCompleteListener.onComplete(true, getNoticeEntity(localData));
                } else {
                    getNoticeFromRemote(new OnCompleteListener<NoticeData>() {
                        @Override
                        public void onComplete(boolean isSuccess, NoticeData remoteData) {
                            onCompleteListener.onComplete(isSuccess, remoteData.toEntity());
                        }
                    }, noticeKey);
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

    public void getNoticeFromLocal(final OnCompleteListener<NoticeData> onCompleteListener, String noticeKey) {
        noticeLocalDataSource.getData(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, noticeKey);
    }

    public void getNoticeFromRemote(final OnCompleteListener<NoticeData> onCompleteListener, String noticeKey) {
        noticeRemoteDataSource.getData(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, noticeKey);
    }

    @NonNull
    private NoticeEntity getNoticeEntity(NoticeData data) {
        return new NoticeEntity(data.getNoticeTitle(), data.getNoticeDesc(), data.getNoticeDate(), data.getNoticeKey());
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