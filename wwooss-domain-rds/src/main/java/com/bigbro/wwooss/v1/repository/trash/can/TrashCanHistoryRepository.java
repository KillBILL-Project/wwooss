package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.trash.can.custom.TrashCanHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashCanHistoryRepository extends JpaRepository<TrashCanHistory, Long>, TrashCanHistoryRepositoryCustom {

    void deleteByUser(User user);

    List<TrashCanHistory> findAllByUser(User user);
}
