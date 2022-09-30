package com.hero.seoultechteams.database.user.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hero.seoultechteams.database.user.dao.UserDao;
import com.hero.seoultechteams.database.user.entity.UserData;

@Database(entities = {UserData.class}, version = 1)
public abstract class AppUserDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AppUserDatabase INSTANCE;

    public static AppUserDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppUserDatabase.class, "tech_users.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract UserDao getUserDao();
}