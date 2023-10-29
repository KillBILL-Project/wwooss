package com.bigbro.wwooss.v1.repository.trash.category;

import com.bigbro.wwooss.v1.domain.entity.trash.category.TrashCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCategoryRepository extends JpaRepository<TrashCategory, Long> {
}
