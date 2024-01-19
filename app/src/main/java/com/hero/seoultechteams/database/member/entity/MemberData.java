package com.hero.seoultechteams.database.member.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.domain.member.entity.MemberEntity;

@Entity(tableName = "member_table")
public class MemberData implements Comparable<MemberData> {

    private String name;
    private String email;
    private String profileImageUrl;
    private String teamKey;

    @PrimaryKey
    @NonNull
    private String key; // memberKey는 userKey와 동일하다.

    @Ignore
    public MemberData() {
    }

    public MemberData(String name, String email, String profileImageUrl, String teamKey, @NonNull String key) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.teamKey = teamKey;
        this.key = key;
    }

    public MemberEntity toEntity() {
        return new MemberEntity(name, email, profileImageUrl, teamKey, key);
    }

    public static MemberData toData(MemberEntity memberEntity) {
        return new MemberData(memberEntity.getName(),
                memberEntity.getEmail(),
                memberEntity.getProfileImageUrl(),
                memberEntity.getTeamKey(),
                memberEntity.getKey());
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