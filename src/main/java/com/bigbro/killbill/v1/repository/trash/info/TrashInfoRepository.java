package com.bigbro.killbill.v1.repository.trash.info;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashInfoRepository extends JpaRepository<TrashInfoEntity, Long> {
    List<TrashInfoEntity> findTrashInfoEntitiesByTrashCategoryEntity(TrashCategoryEntity trashCategoryEntity);
}
