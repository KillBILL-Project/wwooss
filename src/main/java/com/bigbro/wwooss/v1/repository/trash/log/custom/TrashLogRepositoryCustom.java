package com.bigbro.wwooss.v1.repository.trash.log.custom;

import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public interface TrashLogRepositoryCustom {
    Slice<TrashLog> findByUserAndDateBetweenOneMonth(User user, LocalDateTime date, Pageable pageable);
}
