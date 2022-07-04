package com.hero.seoultechteams.database.member.dao;

import androidx.room.Query;

import com.hero.seoultechteams.database.BaseDao;
import com.hero.seoultechteams.database.member.entity.MemberData;

import java.util.List;

public interface MemberDao extends BaseDao<MemberDao> {
    @Query("SELECT * FROM MemberData")
    List<MemberData> getAllMembers();
}