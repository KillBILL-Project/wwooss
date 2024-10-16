package com.bigbro.wwooss.v1.repository.team.custom;

import com.bigbro.wwooss.v1.entity.team.Team;

public interface TeamRepositoryCustom {
    Team findCompanyTeam(String teamName, String address, String businessNo, String email);
    Team findSchoolTeam(String teamName, String address, String email);
    Team findCrewTeam(String teamName, String email);
}
