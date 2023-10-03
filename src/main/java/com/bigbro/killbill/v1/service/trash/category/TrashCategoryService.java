package com.bigbro.killbill.v1.service.trash.category;

import com.bigbro.killbill.v1.domain.request.trash.category.TrashCategoryRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashCategoryResponse;

import java.util.List;


public interface TrashCategoryService {

    /**
     *
     * @return TrashCategoryResponse 쓰레기 카테고리 정보 목록
     */
    List<TrashCategoryResponse> getTrashCategories();

    /**
     *
     * @param trashCategoryRequest 쓰레기 카테고리 생성 정보
     */
    void createTrashCategory(TrashCategoryRequest trashCategoryRequest);
}
