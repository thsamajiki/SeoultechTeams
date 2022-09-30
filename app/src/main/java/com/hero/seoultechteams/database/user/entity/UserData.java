package com.hero.seoultechteams.database.user.entity;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.domain.user.entity.UserEntity;

@Entity
public class UserData {

    private String name;
    private String email;
    private String profileImageUrl;

    @PrimaryKey
    @NonNull
    private String key;

    public UserData(String name, String email, String profileImageUrl, @NonNull String key) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.key = key;
    }

    public UserEntity toEntity() {
        return new UserEntity(name, email, profileImageUrl, key);
    }

    public static UserData toData(UserEntity userEntity) {
        return new UserData(userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getProfileImageUrl(),
                userEntity.getKey());
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof UserData) {
            return getKey().equals(((UserData) obj).getKey());
        }
        return false;
    }
}