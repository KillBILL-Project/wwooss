package com.bigbro.killbill.v1.service.trash.info;

import com.bigbro.killbill.v1.domain.request.trash.info.TrashInfoRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;

import java.util.List;

public interface TrashInfoService {

    /**
     * @param categoryId 쓰레기 카테고리 ID
     *
     * @return TrashInfoResponse List 쓰레기 카테고리 정보 목록
     */
    List<TrashInfoResponse> getTrashInfoByCategoryId(Long categoryId);

    /**
     * @param trashInfoRequest 쓰레기 기본 정보 데이터 ( size , weight etc...)
     *
     * @return TrashInfoResponse 쓰레기 카테고리 정보 목록
     */
    TrashInfoResponse createTrashInfo(TrashInfoRequest trashInfoRequest);
}
