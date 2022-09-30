package com.hero.seoultechteams.domain.notice.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.database.notice.entity.NoticeData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;

public class NoticeEntity implements Parcelable {
    private String noticeTitle;
    private String noticeDesc;
    private String noticeDate;

    @NonNull
    private String noticeKey;

    public NoticeEntity(String noticeTitle, String noticeDesc, String noticeDate, @NonNull String noticeKey) {
        this.noticeTitle = noticeTitle;
        this.noticeDesc = noticeDesc;
        this.noticeDate = noticeDate;
        this.noticeKey = noticeKey;
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

    protected NoticeEntity(Parcel in) {
        noticeTitle = in.readString();
        noticeDesc = in.readString();
        noticeKey = in.readString();
        noticeDate = in.readString();
    }

    public static final Creator<NoticeEntity> CREATOR = new Creator<NoticeEntity>() {
        @Override
        public NoticeEntity createFromParcel(Parcel in) {
            return new NoticeEntity(in);
        }

        @Override
        public NoticeEntity[] newArray(int size) {
            return new NoticeEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noticeTitle);
        dest.writeString(noticeDesc);
        dest.writeString(noticeKey);
    }
}
