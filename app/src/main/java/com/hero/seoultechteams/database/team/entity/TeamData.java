package com.hero.seoultechteams.database.team.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TeamData implements Parcelable {

    private String teamName;
    private String teamDesc;
    private String leaderKey;
    private long createdDate;

    @PrimaryKey
    @NonNull
    private String teamKey;

    public TeamData() {
    }


    protected TeamData(Parcel in) {
        teamName = in.readString();
        teamDesc = in.readString();
        leaderKey = in.readString();
        createdDate = in.readLong();
        teamKey = in.readString();
    }

    public static final Creator<TeamData> CREATOR = new Creator<TeamData>() {
        @Override
        public TeamData createFromParcel(Parcel in) {
            return new TeamData(in);
        }

        @Override
        public TeamData[] newArray(int size) {
            return new TeamData[size];
        }
    };

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

    public void setTeamKey(@NonNull String teamKey) {
        this.teamKey = teamKey;
    }

    public static Creator<TeamData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TeamData) {
            return getTeamKey().equals(((TeamData) obj).getTeamKey());
        }
        return false;
    }
}