package com.hero.seoultechteams.database.member.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MemberData implements Parcelable, Comparable<MemberData> {

    private String name;
    private String email;
    private String profileImageUrl;
    private String teamKey;

    @PrimaryKey
    @NonNull
    private String key; // memberKey는 userKey와 동일하다.

    public MemberData() {
    }

    protected MemberData(Parcel in) {
        name = in.readString();
        email = in.readString();
        profileImageUrl = in.readString();
        teamKey = in.readString();
        key = in.readString();
    }

    public static final Creator<MemberData> CREATOR = new Creator<MemberData>() {
        @Override
        public MemberData createFromParcel(Parcel in) {
            return new MemberData(in);
        }

        @Override
        public MemberData[] newArray(int size) {
            return new MemberData[size];
        }
    };

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
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof MemberData) {
            return getKey().equals(((MemberData) obj).getKey()) && getTeamKey().equals(((MemberData) obj).getTeamKey());
        }
        return false;
    }

    @Override
    public int compareTo(MemberData o) {
        return name.compareTo(o.getName());
    }
}