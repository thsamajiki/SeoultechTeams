package com.hero.seoultechteams.database.notice.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NoticeData implements Parcelable {

    private String noticeTitle;
    private String noticeDesc;
    private String noticeDate;

    @PrimaryKey
    @NonNull
    private String noticeKey;

    public NoticeData() {
    }

    protected NoticeData(Parcel in) {
        noticeTitle = in.readString();
        noticeDesc = in.readString();
        noticeKey = in.readString();
        noticeDate = in.readString();
    }

    public static final Creator<NoticeData> CREATOR = new Creator<NoticeData>() {
        @Override
        public NoticeData createFromParcel(Parcel in) {
            return new NoticeData(in);
        }

        @Override
        public NoticeData[] newArray(int size) {
            return new NoticeData[size];
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