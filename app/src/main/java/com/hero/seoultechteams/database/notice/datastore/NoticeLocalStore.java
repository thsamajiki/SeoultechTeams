package com.hero.seoultechteams.database.notice.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.notice.database.AppNoticeDatabase;
import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public class NoticeLocalStore extends LocalStore<NoticeData> {

    private AppNoticeDatabase appNoticeDatabase;
    private static NoticeLocalStore instance;

    public NoticeLocalStore(Context context, AppNoticeDatabase appNoticeDatabase) {
        super(context);
        this.appNoticeDatabase = appNoticeDatabase;
    }

    private NoticeLocalStore() {
    }

    public static NoticeLocalStore getInstance() {
        if (instance == null) {
            instance = new NoticeLocalStore();
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<NoticeData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String noticeKey = params[0].toString();
                NoticeData noticeData = getNoticeDatabase().getNoticeDao().getNoticeFromKey(noticeKey);
                onCompleteListener.onComplete(true, noticeData);
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<NoticeData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        List<NoticeData> noticeDataList = getNoticeDatabase().getNoticeDao().getAllNotices();
        onCompleteListener.onComplete(true, noticeDataList);
    }

    public AppNoticeDatabase getNoticeDatabase() {
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
