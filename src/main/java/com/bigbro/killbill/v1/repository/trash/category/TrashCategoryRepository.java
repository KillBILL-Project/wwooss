package com.bigbro.killbill.v1.repository.trash.category;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCategoryRepository extends JpaRepository<TrashCategory, Long> {
}
