package com.hero.seoultechteams.database.notice;

import android.content.Context;

import com.hero.seoultechteams.database.CacheStore;
import com.hero.seoultechteams.database.CloudStore;
import com.hero.seoultechteams.database.LocalDataStore;
import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.Repository;
import com.hero.seoultechteams.database.notice.datastore.NoticeCloudStore;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

import java.util.ArrayList;

public class NoticeRepository extends Repository<NoticeData> {

    public NoticeRepository(Context context) {
        super(context);
    }

    @Override
    protected CloudStore<NoticeData> createCloudStore(Context context) {
        return new NoticeCloudStore(context);
    }

    @Override
    protected LocalDataStore<NoticeData> createLocalStore(Context context) {
        return null;
    }

    @Override
    protected CacheStore<NoticeData> createCacheStore() {
        return null;
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

    public void getNoticeList(final OnCompleteListener<ArrayList<NoticeData>> onCompleteListener) {
        getCloudStore().getDataList(onCompleteListener);
    }

    public void getNoticeFromCloud(final OnCompleteListener<NoticeData> onCompleteListener, String noticeKey) {
        getCloudStore().getData(new OnCompleteListener<NoticeData>() {
            @Override
            public void onComplete(boolean isSuccess, NoticeData data) {
                onCompleteListener.onComplete(isSuccess, data);
            }
        }, noticeKey);
    }
}