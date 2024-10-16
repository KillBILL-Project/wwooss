package com.bigbro.wwooss.v1.repository.team;

import com.bigbro.wwooss.v1.entity.teamUser.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
}
