package com.hero.seoultechteams.domain.team.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.hero.seoultechteams.database.team.entity.TeamData;

public class TeamEntity implements Parcelable {
    private String teamName;
    private String teamDesc;
    private String leaderKey;
    private long createdDate;

    @NonNull
    private String teamKey;

    public TeamEntity(String teamName, String teamDesc, String leaderKey, long createdDate, String teamKey) {
        this.teamName = teamName;
        this.teamDesc = teamDesc;
        this.leaderKey = leaderKey;
        this.createdDate = createdDate;
        this.teamKey = teamKey;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDesc() {
        return teamDesc;
    }

    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }

    public String getLeaderKey() {
        return leaderKey;
    }

    public void setLeaderKey(String leaderKey) {
        this.leaderKey = leaderKey;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @NonNull
    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    protected TeamEntity(Parcel in) {
        teamName = in.readString();
        teamDesc = in.readString();
        leaderKey = in.readString();
        createdDate = in.readLong();
        teamKey = in.readString();
    }

    public static final Creator<TeamEntity> CREATOR = new Creator<TeamEntity>() {
        @Override
        public TeamEntity createFromParcel(Parcel in) {
            return new TeamEntity(in);
        }

        @Override
        public TeamEntity[] newArray(int size) {
            return new TeamEntity[size];
        }
    };

    public static Creator<TeamEntity> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teamName);
        dest.writeString(teamDesc);
        dest.writeString(leaderKey);
        dest.writeLong(createdDate);
        dest.writeString(teamKey);
    }
}
