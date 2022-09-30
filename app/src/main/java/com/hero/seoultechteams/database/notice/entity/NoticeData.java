package com.hero.seoultechteams.database.notice.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.domain.notice.entity.NoticeEntity;

@Entity(tableName = "notice_table")
public class NoticeData {

    private String noticeTitle;
    private String noticeDesc;
    private String noticeDate;

    @PrimaryKey
    @NonNull
    private String noticeKey;

    public NoticeData(String noticeTitle, String noticeDesc, String noticeDate, @NonNull String noticeKey) {
        this.noticeTitle = noticeTitle;
        this.noticeDesc = noticeDesc;
        this.noticeDate = noticeDate;
        this.noticeKey = noticeKey;
    }

    public NoticeEntity toEntity() {
        return new NoticeEntity(noticeTitle, noticeDesc, noticeDate, noticeKey);
    }

    public static NoticeData toData(NoticeEntity noticeEntity) {
        return new NoticeData(noticeEntity.getNoticeTitle(),
                noticeEntity.getNoticeDesc(),
                noticeEntity.getNoticeDate(),
                noticeEntity.getNoticeKey());
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeDesc() {
        return noticeDesc;
    }

    public void setNoticeDesc(String noticeDesc) {
        this.noticeDesc = noticeDesc;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    @NonNull
    public String getNoticeKey() {
        return noticeKey;
    }

    public void setNoticeKey(@NonNull String noticeKey) {
        this.noticeKey = noticeKey;
    }
}