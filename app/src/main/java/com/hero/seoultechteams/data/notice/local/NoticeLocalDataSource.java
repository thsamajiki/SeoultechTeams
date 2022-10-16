package com.hero.seoultechteams.data.notice.local;

import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.domain.common.OnCompleteListener;

import java.util.List;

public interface NoticeLocalDataSource {
    // local
    void getData(OnCompleteListener<NoticeData> onCompleteListener, String noticeKey);

    void getDataList(OnCompleteListener<List<NoticeData>> onCompleteListener);
}
