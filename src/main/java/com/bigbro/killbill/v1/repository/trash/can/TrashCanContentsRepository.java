package com.bigbro.killbill.v1.repository.trash.can;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCanContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCanContentsRepository extends JpaRepository<TrashCanContents, Long> {
}
