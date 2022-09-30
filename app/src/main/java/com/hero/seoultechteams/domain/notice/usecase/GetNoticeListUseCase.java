package com.hero.seoultechteams.domain.notice.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;
import com.hero.seoultechteams.domain.notice.repository.NoticeRepository;

import java.util.ArrayList;
import java.util.List;

public class GetNoticeListUseCase {
    private NoticeRepository noticeRepository;

    public GetNoticeListUseCase(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public void invoke(final OnCompleteListener<List<NoticeEntity>> onCompleteListener) {
        noticeRepository.getNoticeList(onCompleteListener);
    }
}
