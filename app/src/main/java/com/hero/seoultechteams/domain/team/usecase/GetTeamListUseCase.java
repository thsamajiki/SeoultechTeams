package com.hero.seoultechteams.domain.team.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

public class GetTeamListUseCase {
    private TeamRepository teamRepository;

    public GetTeamListUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void invoke(final OnCompleteListener<List<TeamEntity>> onCompleteListener, final String userKey) {
        teamRepository.getTeamList(onCompleteListener, userKey);
    }
}
