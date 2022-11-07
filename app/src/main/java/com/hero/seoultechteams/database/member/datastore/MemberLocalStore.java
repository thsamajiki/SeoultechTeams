package com.hero.seoultechteams.database.member.datastore;

import android.content.Context;

import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.member.dao.MemberDao;
import com.hero.seoultechteams.database.member.database.AppMemberDatabase;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.utils.AppExecutors;

import java.util.List;

public class MemberLocalStore extends LocalStore<MemberData> {

    private AppMemberDatabase appMemberDatabase;
    private MemberDao memberDao;
    private static MemberLocalStore instance;
    private final AppExecutors appExecutors = new AppExecutors();

    public MemberLocalStore(Context context, MemberDao memberDao) {
        super(context);
        this.memberDao = memberDao;
    }

    private MemberLocalStore() {
    }

    public static MemberLocalStore getInstance(Context context, MemberDao memberDao) {
        if (instance == null) {
            instance = new MemberLocalStore(context, memberDao);
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<MemberData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String memberKey = params[0].toString();
                MemberData memberData = memberDao.getMemberFromKey(memberKey);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, memberData);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<MemberData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                List<MemberData> memberDataList = memberDao.getAllMembers();

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, memberDataList);
                        }
                    }
                });
            }
        });
    }


    @Override
    public void add(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                memberDao.insertData(data);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, data);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void update(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                memberDao.updateData(data);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete(true, data);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void remove(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        if (data != null) {
            appExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    memberDao.deleteData(data);

                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (onCompleteListener != null) {
                                onCompleteListener.onComplete(true, data);
                            }
                        }
                    });
                }
            });
        }
    }
}
