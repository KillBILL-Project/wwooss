package com.bigbro.wwooss.v1.repository.custom;

import com.bigbro.wwooss.v1.document.TrashCanDocument;
import com.bigbro.wwooss.v1.dto.TrashCanInfo;

import java.util.List;

public interface TrashCanDocumentRepositoryCustom {
    void saveTrashCan(List<TrashCanInfo> trashCanInfoList);

    List<TrashCanDocument> findByGeoLocationAndTrashType(Double lat, Double lng, Integer distance, String trashType);
}
