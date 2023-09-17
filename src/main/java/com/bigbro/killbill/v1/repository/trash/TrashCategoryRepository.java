package com.bigbro.killbill.v1.repository.trash;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCategoryRepository extends JpaRepository<TrashCategoryEntity, Long> {
}
