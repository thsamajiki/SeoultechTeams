package com.hero.seoultechteams.database.user.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.dao.TeamDao;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.todo.entity.TodoData;
import com.hero.seoultechteams.database.user.dao.UserDao;
import com.hero.seoultechteams.database.user.database.AppUserDatabase;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.user.entity.UserData;
import com.hero.seoultechteams.domain.common.OnFailedListener;
import com.hero.seoultechteams.utils.AppExecutors;

import java.util.List;

public class UserLocalStore extends LocalStore<UserData> {

    private UserDao userDao;
    private static UserLocalStore instance;
    private final AppExecutors appExecutors = new AppExecutors();

    private UserLocalStore(Context context, UserDao userDao) {
        // 부모클래스의 생성자를 호출 -> UserLocalStore의 생성자를 통해 받아온 컨텍스트를 LocalStore 생성자의 인자로 넘겨줌.
        super(context);
        this.userDao = userDao;
    }


    public static UserLocalStore getInstance(Context context, UserDao userDao) {
        if (instance == null) {
            instance = new UserLocalStore(context, userDao);
        }
        return instance;
    }

    @Override
    public void getData(final OnCompleteListener<UserData> onCompleteListener, final Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String userKey = params[0].toString();
                UserData userData = userDao.getUserFromKey(userKey);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        onCompleteListener.onComplete(true, userData);
                    }
                });
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<UserData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                List<UserData> userDataList = userDao.getAllUsers();
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        onCompleteListener.onComplete(true, userDataList);
                    }
                });
            }
        });
    }

    @Override
    public void add(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                userDao.insertData(data);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        onCompleteListener.onComplete(true, data);
                    }
                });
            }
        });
    }

    @Override
    public void update(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                userDao.updateData(data);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        onCompleteListener.onComplete(true, data);
                    }
                });
            }
        });
    }

    @Override
    public void remove(OnCompleteListener<UserData> onCompleteListener, UserData data) {
        if (data != null) {
            appExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    userDao.deleteData(data);
                    // 위에 까지는 서브 스레드에서

                    // onCompleteListener는 메인 스레드에서
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            onCompleteListener.onComplete(true, data);
                        }
                    });
                }
            });
        }
    }
}