package com.bigbro.killbill.v1.service.trash.info;

import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;

import java.util.List;

public interface TrashInfoService {

    /**
     * @param categoryId 쓰레기 카테고리 ID
     *
     * @return TrashCategoryResponse 쓰레기 카테고리 정보 목록
     */
    List<TrashInfoResponse> getTrashInfoByCategoryId(Long categoryId);
}
