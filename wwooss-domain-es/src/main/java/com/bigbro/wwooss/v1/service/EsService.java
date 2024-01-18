package com.bigbro.wwooss.v1.service;

import com.bigbro.wwooss.v1.dto.TrashCanInfo;
import java.util.List;


public interface EsService {
    void saveTrashCan(List<TrashCanInfo> trashCanInfoList);
}
