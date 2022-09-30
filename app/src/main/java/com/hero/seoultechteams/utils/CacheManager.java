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
        // 로그인이 안되는 문제가 발생한 이유는 CacheManager를 clear 안해서다 - X
        // 로그인이 안되는 문제가 발생헀는데, CacheManager를 clear 했더니 해결이 됐다 (O)
        // 로그인이 안되는 문제 - Cache가 남아있으면 왜 로그인이 안될까?
        // 코드 순서를 따라가보기도 하고, Log.d를 해서 확인해보기도 하고,
        // BreakPoint를 하거나, debug를 하기도 하는데,

        // Pull Request를 올릴 때 같은 동료가 보는데,
        // 이런 이슈를 해결했는데, 왜 해결됐는지 모르겠는데 해결은 됐다!
        // 이런 얘기를 서로 해보는게 좋음.
        TodoCacheStore.getInstance().clear();
        TeamCacheStore.getInstance().clear();
        UserCacheStore.getInstance().clear();
        MemberCacheStore.getInstance().clear();
    }
}