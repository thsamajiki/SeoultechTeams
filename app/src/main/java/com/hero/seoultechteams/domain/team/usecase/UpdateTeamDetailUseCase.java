package com.hero.seoultechteams.domain.team.usecase;

import com.hero.seoultechteams.domain.common.OnCompleteListener;
import com.hero.seoultechteams.domain.team.entity.TeamEntity;
import com.hero.seoultechteams.domain.team.repository.TeamRepository;

public class UpdateTeamDetailUseCase {
    private final TeamRepository teamRepository;

    public UpdateTeamDetailUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void invoke(final OnCompleteListener<TeamEntity> onCompleteListener, TeamEntity teamEntity) {
        teamRepository.updateTeam(onCompleteListener, teamEntity);
    }
}
