package com.bigbro.wwooss.v1.repository.custom;

import com.bigbro.wwooss.v1.dto.TrashCanInfo;

import java.util.List;

public interface TrashCanDocumentRepositoryCustom {
    void saveTrashCan(List<TrashCanInfo> trashCanInfoList);
}
