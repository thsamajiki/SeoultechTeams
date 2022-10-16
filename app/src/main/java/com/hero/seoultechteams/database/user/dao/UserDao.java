package com.hero.seoultechteams.database.user.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.member.entity.MemberData;
import com.hero.seoultechteams.database.team.entity.TeamData;
import com.hero.seoultechteams.database.user.entity.UserData;

import java.util.List;

@Dao
public interface UserDao extends BaseDao<UserData> {
    @Query("SELECT * FROM user_table")
    List<UserData> getAllUsers();

    @Query("SELECT * FROM user_table WHERE `key` = :userKey limit 1")
    UserData getUserFromKey(String userKey);
}
