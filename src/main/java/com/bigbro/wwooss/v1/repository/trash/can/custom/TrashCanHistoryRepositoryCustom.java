package com.bigbro.wwooss.v1.repository.trash.can.custom;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TrashCanHistoryRepositoryCustom {
    Slice<TrashCanHistory> findTrashCanHistoriesByUserAndDate(User user, String date, Pageable pageable);
}
