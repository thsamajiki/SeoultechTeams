package com.hero.seoultechteams.database.member.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.List;

@Dao
public interface MemberDao extends BaseDao<MemberData> {
    @Query("SELECT * FROM member_table")
    List<MemberData> getAllMembers();

    @Query("SELECT * FROM member_table WHERE `key` = :memberKey limit 1")
    MemberData getMemberFromKey(String memberKey);
}
