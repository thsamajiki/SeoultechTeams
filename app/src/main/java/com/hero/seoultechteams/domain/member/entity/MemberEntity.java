package com.hero.seoultechteams.domain.member.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.database.member.entity.MemberData;

public class MemberEntity implements Parcelable, Comparable<MemberEntity> {
    private String name;
    private String email;
    private String profileImageUrl;
    private String teamKey;

    @NonNull
    private String key; // memberKey는 userKey와 동일하다.

    public MemberEntity() {
    }

    public MemberEntity(String name, String email, String profileImageUrl, String teamKey, @NonNull String key) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.teamKey = teamKey;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    protected MemberEntity(Parcel in) {
        name = in.readString();
        email = in.readString();
        profileImageUrl = in.readString();
        teamKey = in.readString();
        key = in.readString();
    }

    public static final Creator<MemberEntity> CREATOR = new Creator<MemberEntity>() {
        @Override
        public MemberEntity createFromParcel(Parcel in) {
            return new MemberEntity(in);
        }

        @Override
        public MemberEntity[] newArray(int size) {
            return new MemberEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(profileImageUrl);
        dest.writeString(teamKey);
        dest.writeString(key);
    }

    @Override
    public int compareTo(MemberEntity o) {
        return name.compareTo(o.getName());
    }
}