package com.hero.seoultechteams.data.notice.remote;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

import java.util.ArrayList;
import java.util.List;

public interface NoticeRemoteDataSource {
    void getData(OnCompleteListener<NoticeData> onCompleteListener, String noticeKey);

    void getDataList(OnCompleteListener<List<NoticeData>> onCompleteListener);
}
