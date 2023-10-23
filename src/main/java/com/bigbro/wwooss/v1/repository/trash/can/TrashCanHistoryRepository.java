package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.repository.trash.can.custom.TrashCanHistoryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCanHistoryRepository extends JpaRepository<TrashCanHistory, Long>, TrashCanHistoryRepositoryCustom {
}
