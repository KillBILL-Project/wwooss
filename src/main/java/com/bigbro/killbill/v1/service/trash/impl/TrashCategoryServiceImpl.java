package com.bigbro.killbill.v1.service.trash.impl;

import com.bigbro.killbill.v1.common.KillBillResponseCode;
import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfoEntity;
import com.bigbro.killbill.v1.domain.request.trash.TrashCategoryRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashCategoryResponse;
import com.bigbro.killbill.v1.domain.response.trash.TrashCategoryWithInfoResponse;
import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.killbill.v1.exception.DataNotFoundException;
import com.bigbro.killbill.v1.repository.trash.TrashCategoryRepository;
import com.bigbro.killbill.v1.service.trash.TrashCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryServiceImpl implements TrashCategoryService {

    private final TrashCategoryRepository trashCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TrashCategoryResponse> getTrashCategories() {
        List<TrashCategoryEntity> trashCategoryResponses = trashCategoryRepository.findAll();
        return trashCategoryResponses.stream().map(TrashCategoryResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrashCategoryWithInfoResponse getTrashCategoryByCategoryId(Long categoryId) {
        TrashCategoryEntity trashCategoryEntity = trashCategoryRepository.findById(categoryId).orElseThrow(() -> new DataNotFoundException(KillBillResponseCode.NOT_FOUND_DATA, "해당 쓰레기 카테고리가 존재하지 않습니다."));
        List<TrashInfoResponse> trashInfoResponseList = trashCategoryEntity.getTrashInfoEntityList().stream().map(TrashInfoResponse::from).toList();

        return TrashCategoryWithInfoResponse.from(categoryId, trashInfoResponseList);
    }

    @Override
    @Transactional
    public void createTrashCategory(TrashCategoryRequest trashCategoryRequest) {
        trashCategoryRepository.save(TrashCategoryEntity.from(trashCategoryRequest));
    }
}
