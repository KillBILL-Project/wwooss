package com.bigbro.wwooss.v1.repository.trash.log;

import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.trash.log.custom.TrashLogRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashLogRepository extends JpaRepository<TrashLog, Long>, TrashLogRepositoryCustom {

    List<TrashLog> findAllByUserAndTrashCanHistoryNull(User user);

    List<TrashLog> findByUser(User user);

    void deleteByUser(User user);

    List<TrashLog> findTrashLogByUserAndTrashCanHistory(User user, TrashCanHistory trashCanHistory);

}
