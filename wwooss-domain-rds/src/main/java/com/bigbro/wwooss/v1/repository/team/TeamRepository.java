package com.bigbro.wwooss.v1.repository.team;

import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.enumType.TeamType;
import com.bigbro.wwooss.v1.repository.team.custom.TeamRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    Optional<Team> findByTeamIdAndTeamType(Long teamId, TeamType teamType);

    Slice<Team> findTeamByTeamTypeAndTeamNameContaining(TeamType teamType, String teamName, Pageable pageable);
}
