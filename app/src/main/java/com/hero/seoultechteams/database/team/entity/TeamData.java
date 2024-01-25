package com.hero.seoultechteams.database.team.entity;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.hero.seoultechteams.domain.team.entity.TeamEntity;

@Keep
@Entity(tableName = "team_table")
public class TeamData {

    private String teamName;
    private String teamDesc;
    private String leaderKey;
    private long createdDate;

    @PrimaryKey
    @NonNull
    private String teamKey;

    @Ignore
    public TeamData() {
    }

    public TeamData(String teamName, String teamDesc, String leaderKey, long createdDate, @NonNull String teamKey) {
        this.teamName = teamName;
        this.teamDesc = teamDesc;
        this.leaderKey = leaderKey;
        this.createdDate = createdDate;
        this.teamKey = teamKey;
    }

    public TeamEntity toEntity() {
        return new TeamEntity(teamName, teamDesc, leaderKey, createdDate, teamKey);
    }

    public static TeamData toData(TeamEntity teamEntity) {
        return new TeamData(teamEntity.getTeamName(),
                teamEntity.getTeamDesc(),
                teamEntity.getLeaderKey(),
                teamEntity.getCreatedDate(),
                teamEntity.getTeamKey());
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



    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TeamData) {
            return getTeamKey().equals(((TeamData) obj).getTeamKey());
        }
        return false;
    }
}
