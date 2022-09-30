package com.hero.seoultechteams.domain.user.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserEntity implements Parcelable {
    private String name;
    private String email;
    private String profileImageUrl;

    @NonNull
    private String key;

    public UserEntity() {
    }

    public UserEntity(String name, String email, String profileImageUrl, @NonNull String key) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
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

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    protected UserEntity(Parcel in) {
        name = in.readString();
        email = in.readString();
        profileImageUrl = in.readString();
        key = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
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
        dest.writeString(key);
    }
}
