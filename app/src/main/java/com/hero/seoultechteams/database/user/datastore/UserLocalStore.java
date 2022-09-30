package com.hero.seoultechteams.database.user.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.user.database.AppUserDatabase;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.List;

public class UserLocalStore extends LocalStore<UserData> {

    private AppUserDatabase appUserDatabase;
    private static UserLocalStore instance;

    public UserLocalStore(Context context, AppUserDatabase appUserDatabase) {
        // 부모클래스의 생성자를 호출 -> UserLocalStore의 생성자를 통해 받아온 컨텍스트를 LocalStore 생성자의 인자로 넘겨줌.
        super(context);
        this.appUserDatabase = appUserDatabase;
    }

    private UserLocalStore() {
    }

    public static UserLocalStore getInstance() {
        if (instance == null) {
            instance = new UserLocalStore();
        }
        return instance;
    }

    @Override
    public void getData(final OnCompleteListener<UserData> onCompleteListener, final Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String userKey = params[0].toString();
                UserData userData = getUserDataBase().getUserDao().getUserFromKey(userKey);
                onCompleteListener.onComplete(true, userData);
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<UserData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        List<UserData> userDataList = getUserDataBase().getUserDao().getAllUsers();
        onCompleteListener.onComplete(true, userDataList);
    }

    public AppUserDatabase getUserDataBase() {
        return appUserDatabase;
    }

    @Override
    public void add(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        getUserDataBase().getUserDao().insertData(data);
    }

    @Override
    public void update(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        getUserDataBase().getUserDao().updateData(data);
    }

    @Override
    public void remove(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        if (data != null) {
            getUserDataBase().getUserDao().deleteData(data);
        }
    }
}