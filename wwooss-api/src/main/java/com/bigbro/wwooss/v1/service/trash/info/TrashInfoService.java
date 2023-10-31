package com.bigbro.wwooss.v1.service.trash.info;

import com.bigbro.wwooss.v1.dto.request.trash.info.TrashInfoRequest;
import com.bigbro.wwooss.v1.dto.response.trash.TrashInfoResponse;

import java.util.List;

public interface TrashInfoService {

    /**
     * @param categoryId 쓰레기 카테고리 ID
     *
     * @return TrashInfoResponse List 쓰레기 카테고리 정보 목록
     */
    List<TrashInfoResponse> getTrashInfoByCategoryId(Long categoryId);

    /**
     * @param trashInfoRequest 쓰레기 기본 정보 데이터 ( weight etc...)
     *
     * @return TrashInfoResponse 쓰레기 카테고리 정보 목록
     */
    TrashInfoResponse createTrashInfo(TrashInfoRequest trashInfoRequest);
}
