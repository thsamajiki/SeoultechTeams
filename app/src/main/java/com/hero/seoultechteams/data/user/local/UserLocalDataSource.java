package com.hero.seoultechteams.data.user.local;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.domain.user.entity.UserEntity;

public interface UserLocalDataSource {
    void getData(OnCompleteListener<UserData> onCompleteListener, String userKey);

    void add(final OnCompleteListener<UserData> onCompleteListener, OnFailedListener onFailedListener, String userName, String email, String pwd);

    void update(final OnCompleteListener<UserData> onCompleteListener, UserData userData);

    void remove(final OnCompleteListener<UserData> onCompleteListener, UserData userData);

    void signOut();
}
