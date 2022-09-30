package com.hero.seoultechteams.database.member.datastore;

import android.content.Context;
import android.os.AsyncTask;

import com.hero.seoultechteams.database.LocalStore;
import com.hero.seoultechteams.database.member.database.AppMemberDatabase;
import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.List;

public class MemberLocalStore extends LocalStore<MemberData> {

    private AppMemberDatabase appMemberDatabase;
    private static MemberLocalStore instance;

    public MemberLocalStore(Context context, AppMemberDatabase appMemberDatabase) {
        super(context);
        this.appMemberDatabase = appMemberDatabase;
    }

    private MemberLocalStore() {
    }

    public static MemberLocalStore getInstance() {
        if (instance == null) {
            instance = new MemberLocalStore();
        }
        return instance;
    }

    @Override
    public void getData(OnCompleteListener<MemberData> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String memberKey = params[0].toString();
                MemberData memberData = getMemberDatabase().getMemberDao().getMemberFromKey(memberKey);
                onCompleteListener.onComplete(true, memberData);
            }
        });
    }

    @Override
    public void getDataList(OnCompleteListener<List<MemberData>> onCompleteListener, Object... params) {
        if (params == null || params.length == 0) {
            onCompleteListener.onComplete(false, null);
            return;
        }

        List<MemberData> memberDataList = getMemberDatabase().getMemberDao().getAllMembers();
        onCompleteListener.onComplete(true, memberDataList);
    }

    public AppMemberDatabase getMemberDatabase() {
        return appMemberDatabase;
    }


    @Override
    public void add(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        getMemberDatabase().getMemberDao().insertData(data);
    }

    @Override
    public void update(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        getMemberDatabase().getMemberDao().updateData(data);
    }

    @Override
    public void remove(OnCompleteListener<MemberData> onCompleteListener, MemberData data) {
        if (data != null) {
            getMemberDatabase().getMemberDao().deleteData(data);
        }
    }
}
