package com.hero.seoultechteams.data.user.local;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.user.datastore.UserCacheStore;
import com.hero.seoultechteams.database.user.datastore.UserLocalStore;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.utils.CacheManager;

public class UserLocalDataSourceImpl implements UserLocalDataSource {
    private final UserCacheStore userCacheStore;
    private final UserLocalStore userLocalStore;

    public UserLocalDataSourceImpl(UserLocalStore userLocalStore, UserCacheStore userCacheStore) {
        this.userLocalStore = userLocalStore;
        this.userCacheStore = userCacheStore;
    }

    @Override
    public void getData(OnCompleteListener<UserData> onCompleteListener, String userKey) {
        userCacheStore.getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    userLocalStore.getData(new OnCompleteListener<UserData>() {
                        @Override
                        public void onComplete(boolean isSuccess, UserData data) {
                            if (isSuccess) {
                                onCompleteListener.onComplete(true, data);
                            } else {

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void add(OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd) {
//        userCacheStore.add(onCompleteListener, onFailedListener, userName, email, pwd);
//        userLocalStore.add(onCompleteListener, onFailedListener, userName, email, pwd);
    }

    @Override
    public void update(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        userCacheStore.update(onCompleteListener, userData);
        userLocalStore.update(onCompleteListener, userData);
    }

    @Override
    public void remove(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        userCacheStore.remove(onCompleteListener, userData);
        userLocalStore.remove(onCompleteListener, userData);
    }

    @Override
    public void signOut() {
        CacheManager.getInstance().allClear();
    }
}
