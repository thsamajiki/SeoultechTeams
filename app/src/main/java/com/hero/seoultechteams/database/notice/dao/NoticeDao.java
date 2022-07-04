package com.hero.seoultechteams.database.notice.dao;

import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.notice.entity.NoticeData;

import java.util.List;

public interface NoticeDao extends BaseDao<NoticeDao> {
    @Query("SELECT * FROM NoticeData")
    List<NoticeData> getAllNotices();
}