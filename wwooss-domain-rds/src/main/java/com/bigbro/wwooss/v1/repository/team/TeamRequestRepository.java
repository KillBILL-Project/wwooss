package com.bigbro.wwooss.v1.repository.team;

import com.bigbro.wwooss.v1.entity.teamRequest.TeamRequest;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {
    List<TeamRequest> findByRequestUserAndStatusIn(User user, List<RequestStatus> status);
}
