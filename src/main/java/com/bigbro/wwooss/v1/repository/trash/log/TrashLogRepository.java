package com.bigbro.wwooss.v1.repository.trash.log;

import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashLogRepository extends JpaRepository<TrashLog, Long> {
}
