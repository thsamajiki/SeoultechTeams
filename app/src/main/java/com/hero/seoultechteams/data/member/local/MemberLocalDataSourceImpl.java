package com.hero.seoultechteams.data.member.local;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.datastore.MemberCacheStore;
import com.hero.seoultechteams.database.member.datastore.MemberLocalStore;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.ArrayList;
import java.util.List;

public class MemberLocalDataSourceImpl implements MemberLocalDataSource {
    private final MemberCacheStore memberCacheStore;
    private final MemberLocalStore memberLocalStore;

    public MemberLocalDataSourceImpl(MemberLocalStore memberLocalStore, MemberCacheStore memberCacheStore) {
        this.memberLocalStore = memberLocalStore;
        this.memberCacheStore = memberCacheStore;
    }

    @Override
    public void getData(OnCompleteListener<MemberData> onCompleteListener, String teamKey) {
        memberCacheStore.getData(new OnCompleteListener<MemberData>() {
            @Override
            public void onComplete(boolean isSuccess, MemberData data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    memberLocalStore.getData(new OnCompleteListener<MemberData>() {
                        @Override
                        public void onComplete(boolean isSuccess, MemberData data) {
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
    public void getDataList(OnCompleteListener<List<MemberData>> onCompleteListener, String teamKey) {
        memberCacheStore.getDataList(new OnCompleteListener<List<MemberData>>() {
            @Override
            public void onComplete(boolean isSuccess, List<MemberData> data) {
                if (isSuccess) {
                    onCompleteListener.onComplete(true, data);
                } else {
                    memberLocalStore.getDataList(new OnCompleteListener<List<MemberData>>() {
                        @Override
                        public void onComplete(boolean isSuccess, List<MemberData> data) {
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
}
