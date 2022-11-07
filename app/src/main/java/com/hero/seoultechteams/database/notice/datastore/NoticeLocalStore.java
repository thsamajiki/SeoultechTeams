package com.hero.seoultechteams.database.notice.datastore;

import android.content.Context;

import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.notice.dao.NoticeDao;
import com.hero.seoultechteams.database.notice.database.AppNoticeDatabase;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.utils.AppExecutors;

import java.util.List;

public class NoticeLocalStore extends LocalStore<NoticeData> {

    private AppNoticeDatabase appNoticeDatabase;
    private NoticeDao noticeDao;
    private static NoticeLocalStore instance;
    private final AppExecutors appExecutors = new AppExecutors();

    public NoticeLocalStore(Context context, NoticeDao noticeDao) {
        super(context);
        this.noticeDao = noticeDao;
    }

    private NoticeLocalStore() {
    }

    public static NoticeLocalStore getInstance(Context context, NoticeDao noticeDao) {
        if (instance == null) {
            instance = new NoticeLocalStore(context, noticeDao);
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<NoticeData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String noticeKey = params[0].toString();
                NoticeData noticeData = noticeDao.getNoticeFromKey(noticeKey);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        onCompleteListener.onComplete(true, noticeData);
                    }
                });
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<NoticeData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                List<NoticeData> noticeDataList = noticeDao.getAllNotices();

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        onCompleteListener.onComplete(true, noticeDataList);
                    }
                });
            }
        });
    }

    private AppNoticeDatabase getNoticeDatabase() {
        return appNoticeDatabase;
    }

    @Override
    public void add(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }

    @Override
    public void update(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }

    @Override
    public void remove(OnCompleteListener<NoticeData> onCompleteListener, NoticeData data) {

    }
}
