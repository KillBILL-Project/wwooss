package com.bigbro.wwooss.v1.repository.trash.log.custom;

import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TrashLogRepositoryCustom {
    Page<TrashLog> findByUserAndDate(User user, String date, Pageable pageable);

    List<TrashLog> findTrashLogByUserAtLastWeek(User user, LocalDate toDay);
}
