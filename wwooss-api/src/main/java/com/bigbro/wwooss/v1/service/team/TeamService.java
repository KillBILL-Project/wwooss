package com.bigbro.wwooss.v1.service.team;

import com.bigbro.wwooss.v1.dto.request.team.CreateTeamRequest;

public interface TeamService {

    void createTeam(Long userId, CreateTeamRequest createTeamRequest);
}
