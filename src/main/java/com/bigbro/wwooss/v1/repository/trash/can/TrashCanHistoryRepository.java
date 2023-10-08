package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCanHistoryRepository extends JpaRepository<TrashCanHistory, Long> {
}
