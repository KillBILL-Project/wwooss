package com.bigbro.wwooss.v1.repository.trash.log.custom;

import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TrashLogRepositoryCustom {
    Slice<TrashLog> findByUserAndDate(User user, String date, Pageable pageable);
}
