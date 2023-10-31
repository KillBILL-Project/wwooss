package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.repository.trash.can.custom.TrashCanHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCanHistoryRepository extends JpaRepository<TrashCanHistory, Long>, TrashCanHistoryRepositoryCustom {
}
