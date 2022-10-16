package com.hero.seoultechteams.database.notice.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hero.seoultechteams.database.notice.dao.NoticeDao;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

@Database(entities = {NoticeData.class}, version = 1)
public abstract class AppNoticeDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AppNoticeDatabase INSTANCE;

    public static AppNoticeDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppNoticeDatabase.class, "notice_table.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract NoticeDao getNoticeDao();
}