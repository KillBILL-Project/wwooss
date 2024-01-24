package com.bigbro.wwooss.v1.service.trash.info;

import com.bigbro.wwooss.v1.dto.request.trash.info.TrashInfoRequest;
import com.bigbro.wwooss.v1.dto.response.trash.TrashInfoResponse;

import java.util.List;

public interface TrashInfoService {

    /**
     * 모든 쓰레기 메타 정보 조회
     */
    List<TrashInfoResponse> getTrashInfo();

    /**
     * @param trashInfoRequest 쓰레기 기본 정보 데이터 ( weight etc...)
     *
     * @return TrashInfoResponse 쓰레기 카테고리 정보 목록
     */
    void createTrashInfo(TrashInfoRequest trashInfoRequest);
}
