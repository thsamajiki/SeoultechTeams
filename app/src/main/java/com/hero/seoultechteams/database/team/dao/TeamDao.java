package com.hero.seoultechteams.database.team.dao;

import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.team.entity.TeamData;

import java.util.List;

public interface TeamDao extends BaseDao<TeamDao> {
    @Query("SELECT * FROM TeamData")
    List<TeamData> getAllTeams();

    @Query("SELECT * FROM TeamData WHERE userKey = :userKey limit 1")
    TeamData getTeamFromKey(String userKey);
}