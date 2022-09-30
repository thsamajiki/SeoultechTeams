package com.hero.seoultechteams.database.team.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.List;

@Dao
public interface TeamDao extends BaseDao<TeamData> {
    @Query("SELECT * FROM team_table")
    List<TeamData> getAllTeams();

    @Query("SELECT * FROM team_table WHERE userKey = :userKey limit 1")
    TeamData getTeamFromKey(String userKey);
}