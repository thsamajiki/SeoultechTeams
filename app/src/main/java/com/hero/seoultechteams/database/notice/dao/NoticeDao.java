package com.hero.seoultechteams.database.notice.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

import java.util.List;

@Dao
public interface NoticeDao extends BaseDao<NoticeData> {
    @Query("SELECT * FROM notice_table")
    List<NoticeData> getAllNotices();

    @Query("SELECT * FROM notice_table WHERE noticeKey = :noticeKey limit 1")
    NoticeData getNoticeFromKey(String noticeKey);
}