package com.hero.seoultechteams.data.user.local;

import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.common.OnFailedListener;

public interface UserLocalDataSource {
    void getData(OnCompleteListener<UserData> onCompleteListener, String userKey);

    void addUser(final OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, UserData userData);

    void updateUser(final OnCompleteListener<UserData> onCompleteListener, UserData userData);

    void removeUser(final OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, UserData userData);

    void signOut();
}
