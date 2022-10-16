package com.hero.seoultechteams.data.user.local;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.user.datastore.UserLocalStore;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.exception.FailedAddLocalUserException;
import com.hero.seoultechteams.utils.CacheManager;

public class UserLocalDataSourceImpl implements UserLocalDataSource {
    private final UserLocalStore userLocalStore;

    public UserLocalDataSourceImpl(UserLocalStore userLocalStore) {
        this.userLocalStore = userLocalStore;
    }

    @Override
    public void getData(OnCompleteListener<UserData> onCompleteListener, String userKey) {
        userLocalStore.getData(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        });
    }

    @Override
    public void addUser(OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, UserData userData) {
        userLocalStore.add(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData localData) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, localData);
                } else {
                    onFailedListener.onFailed(new FailedAddLocalUserException("failed add"));
                }
            }
        }, userData);
    }

    @Override
    public void updateUser(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        userLocalStore.update(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData localData) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, localData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, userData);
    }

    @Override
    public void removeUser(OnCompleteListener<UserData> onCompleteListener, UserData userData) {
        userLocalStore.remove(new OnCompleteListener<UserData>() {
            @Override
            public void onComplete(boolean isSuccess, UserData localData) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, localData);
                } else {
                    onCompleteListener.onComplete(false, null);
                }
            }
        }, userData);
    }

    @Override
    public void signOut() {
        CacheManager.getInstance().allClear();
    }
}
