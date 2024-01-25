package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.entity.trash.can.TrashCan;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrashCanRepository extends JpaRepository<TrashCan, Long> {
    Slice<TrashCan> findBySaveCompleted(Boolean saveCompleted, Pageable pageable);

    List<TrashCan> findAllBySaveCompleted(Boolean saveCompleted);
}
