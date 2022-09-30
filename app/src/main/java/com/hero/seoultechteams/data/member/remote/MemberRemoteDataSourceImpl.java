package com.hero.seoultechteams.data.member.remote;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.datastore.MemberCloudStore;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.ArrayList;
import java.util.List;

public class MemberRemoteDataSourceImpl implements MemberRemoteDataSource {
    private final MemberCloudStore memberCloudStore;

    public MemberRemoteDataSourceImpl(MemberCloudStore memberCloudStore) {
        this.memberCloudStore = memberCloudStore;
    }

    @Override
    public void getData(OnCompleteListener<MemberData> onCompleteListener, String teamKey) {
        memberCloudStore.getData(onCompleteListener, teamKey);
    }

    @Override
    public void getDataList(OnCompleteListener<List<MemberData>> onCompleteListener, String teamKey) {
        memberCloudStore.getDataList(onCompleteListener, teamKey);
    }
}
