package com.bigbro.wwooss.v1.repository.trash.log;

import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashLogRepository extends JpaRepository<TrashLog, Long> {

    List<TrashLog> findAllByUserAndTrashCanHistoryNull(User user);
}