package com.hero.seoultechteams.database.user.entity;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserData implements Parcelable {

    private String name;
    private String email;
    private String profileImageUrl;

    @PrimaryKey
    @NonNull
    private String key;

    public UserData() {
    }

    protected UserData(Parcel in) {
        name = in.readString();
        email = in.readString();
        profileImageUrl = in.readString();
        key = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
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
        dest.writeString(key);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof UserData) {
            return getKey().equals(((UserData) obj).getKey());
        }
        return false;
    }
}