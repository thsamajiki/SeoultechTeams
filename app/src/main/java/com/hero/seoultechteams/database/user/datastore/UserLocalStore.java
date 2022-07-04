package com.hero.seoultechteams.database.user.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.OnCompleteListener;
import com.hero.seoultechteams.database.LocalDataStore;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.ArrayList;
import java.util.List;

public class UserLocalStore extends LocalDataStore<UserData> {

    public UserLocalStore(Context context) {
        // 부모클래스의 생성자를 호출 -> UserLocalStore의 생성자를 통해 받아온 컨텍스트를 LocalDataStore 생성자의 인자로 넘겨줌.
        super(context);
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
                UserData userData = getAppDataBase().getUserDao().getUserFromKey(userKey);
                onCompleteListener.onComplete(true, userData);
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<ArrayList<UserData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        List<UserData> userDataList = getAppDataBase().getUserDao().getAllUsers();
        onCompleteListener.onComplete(true, (ArrayList<UserData>) userDataList);
    }

    @Override
    public void add(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        getAppDataBase().getUserDao().insertData(data);
    }

    @Override
    public void update(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        getAppDataBase().getUserDao().insertData(data);
    }

    @Override
    public void remove(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        if (data != null) {
            getAppDataBase().getUserDao().delete(data);
        }
    }
}