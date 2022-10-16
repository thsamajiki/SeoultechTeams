package com.hero.seoultechteams.database.todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hero.seoultechteams.database.todo.dao.TodoDao;
import com.hero.seoultechteams.database.todo.entity.TodoData;

@Database(entities = {TodoData.class}, version = 1)
public abstract class AppTodoDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AppTodoDatabase INSTANCE;

    public static AppTodoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppTodoDatabase.class, "todo_table.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract TodoDao getTodoDao();
}
