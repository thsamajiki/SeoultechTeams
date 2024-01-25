package com.hero.seoultechteams.domain.notice.repository;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;

import java.util.ArrayList;
import java.util.List;

public interface NoticeRepository {
    void getNoticeList(final OnCompleteListener<List<NoticeEntity>> onCompleteListener);
}
