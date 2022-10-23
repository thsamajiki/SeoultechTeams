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
//                MemberData memberData = getMemberDatabase().getMemberDao().getMemberFromKey(memberKey);
                MemberData memberData = memberDao.getMemberFromKey(memberKey);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
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
//                List<MemberData> memberDataList = getMemberDatabase().getMemberDao().getAllMembers();
                List<MemberData> memberDataList = memberDao.getAllMembers();
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
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

//    private AppMemberDatabase getMemberDatabase() {
//        return appMemberDatabase;
//    }


    @Override
    public void add(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
//                getMemberDatabase().getMemberDao().insertData(data);
                memberDao.insertData(data);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
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
//                getMemberDatabase().getMemberDao().updateData(data);
                memberDao.updateData(data);
                // 위에 까지는 서브 스레드에서

                // onCompleteListener는 메인 스레드에서
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
//                    getMemberDatabase().getMemberDao().deleteData(data);
                    memberDao.deleteData(data);
                    // 위에 까지는 서브 스레드에서

                    // onCompleteListener는 메인 스레드에서
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
