package com.bigbro.wwooss.v1.repository.team;

import com.bigbro.wwooss.v1.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
