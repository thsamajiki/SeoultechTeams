package com.hero.seoultechteams.database.team.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hero.seoultechteams.database.team.dao.TeamDao;
import com.hero.seoultechteams.database.team.entity.TeamData;

@Database(entities = {TeamData.class}, version = 1)
public abstract class AppTeamDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AppTeamDatabase INSTANCE;

    public static AppTeamDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppTeamDatabase.class, "team_table.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract TeamDao getTeamDao();
}
