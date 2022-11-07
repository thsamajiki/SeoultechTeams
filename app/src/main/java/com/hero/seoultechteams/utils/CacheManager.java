package com.hero.seoultechteams.utils;

import com.hero.seoultechteams.database.member.datastore.MemberCacheStore;
import com.hero.seoultechteams.database.team.datastore.TeamCacheStore;
import com.hero.seoultechteams.database.todo.datastore.TodoCacheStore;
import com.hero.seoultechteams.database.user.datastore.UserCacheStore;

public class CacheManager {

    private static CacheManager instance;

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public void allClear() {
        TodoCacheStore.getInstance().clear();
        TeamCacheStore.getInstance().clear();
        UserCacheStore.getInstance().clear();
        MemberCacheStore.getInstance().clear();
    }
}