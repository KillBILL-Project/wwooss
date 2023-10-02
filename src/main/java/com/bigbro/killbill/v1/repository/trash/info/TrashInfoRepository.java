package com.bigbro.killbill.v1.repository.trash.info;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategory;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashInfoRepository extends JpaRepository<TrashInfo, Long> {
    List<TrashInfo> findTrashInfoEntitiesByTrashCategoryId(TrashCategory trashCategory);
}
