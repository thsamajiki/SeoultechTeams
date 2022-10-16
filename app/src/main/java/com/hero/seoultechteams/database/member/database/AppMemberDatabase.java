package com.hero.seoultechteams.database.member.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hero.seoultechteams.database.member.dao.MemberDao;
import com.hero.seoultechteams.database.member.entity.MemberData;

@Database(entities = {MemberData.class}, version = 1)
public abstract class AppMemberDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AppMemberDatabase INSTANCE;

    public static AppMemberDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppMemberDatabase.class, "member_table.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract MemberDao getMemberDao();
}