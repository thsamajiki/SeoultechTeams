package com.hero.seoultechteams.domain.team.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.repository.TeamRepository;

public class GetTeamUseCase {
    private final TeamRepository teamRepository;

    public GetTeamUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void invoke(final OnCompleteListener<TeamEntity> onCompleteListener, String teamKey) {
        teamRepository.getTeam(onCompleteListener, teamKey);
    }
}
