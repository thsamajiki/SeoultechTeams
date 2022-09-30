package com.hero.seoultechteams.data.member.remote;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.ArrayList;
import java.util.List;

public interface MemberRemoteDataSource {
    // CloudStore
    void getData(OnCompleteListener<MemberData> onCompleteListener, String teamKey);

    void getDataList(OnCompleteListener<List<MemberData>> onCompleteListener, String teamKey);
}
