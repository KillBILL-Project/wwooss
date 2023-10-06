package com.bigbro.wwooss.v1.repository.trash.can;

import com.bigbro.wwooss.v1.domain.entity.trash.TrashCanContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCanContentsRepository extends JpaRepository<TrashCanContents, Long> {
}
