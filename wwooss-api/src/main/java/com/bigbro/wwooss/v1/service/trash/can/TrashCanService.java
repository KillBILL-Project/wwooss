package com.bigbro.wwooss.v1.service.trash.can;

import com.bigbro.wwooss.v1.dto.TrashCanInfoList;
import org.springframework.data.domain.Pageable;

public interface TrashCanService {

    /**
     * 쓰레기 정보 가져오기
     */
    TrashCanInfoList getTrashCanInfo(Pageable pageable);
}
