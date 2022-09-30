package com.hero.seoultechteams.domain.team.repository;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;

import java.util.ArrayList;
import java.util.List;

public interface TeamRepository {
    void getTeamList(final OnCompleteListener<List<TeamEntity>> onCompleteListener, final String userKey);

    void addTeam(final OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity);

    void updateTeam(final OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity);

    void removeTeam(final OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity);
}
