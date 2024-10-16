package com.bigbro.wwooss.v1.repository.team;

import com.bigbro.wwooss.v1.entity.teamUser.TeamUser;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findByUser(User user);
}
