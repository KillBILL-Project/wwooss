package com.bigbro.killbill.v1.service.trash.impl;

import com.bigbro.killbill.v1.common.KillBillResponseCode;
import com.bigbro.killbill.v1.domain.entity.trash.TrashCategory;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfo;
import com.bigbro.killbill.v1.domain.request.trash.info.TrashInfoRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.killbill.v1.exception.DataNotFoundException;
import com.bigbro.killbill.v1.repository.trash.category.TrashCategoryRepository;
import com.bigbro.killbill.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.killbill.v1.service.trash.info.TrashInfoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashInfoServiceImpl implements TrashInfoService {

    private final TrashCategoryRepository trashCategoryRepository;

    private final TrashInfoRepository trashInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TrashInfoResponse> getTrashInfoByCategoryId(Long categoryId) {
        TrashCategory trashCategory = trashCategoryRepository.findById(categoryId).orElseThrow(() -> new DataNotFoundException(KillBillResponseCode.NOT_FOUND_DATA, "해당 쓰레기 카테고리가 존재하지 않습니다."));
        List<TrashInfo> trashInfoList = trashInfoRepository.findTrashInfoEntitiesByTrashCategory(trashCategory);

        return trashInfoList.stream().map(TrashInfoResponse::from).toList();
    }

    @Override
    @Transactional
    public TrashInfoResponse createTrashInfo(TrashInfoRequest trashInfoRequest) {
        TrashCategory trashCategory = trashCategoryRepository.findById(trashInfoRequest.getTrashCategoryId()).orElseThrow(() -> new DataNotFoundException(KillBillResponseCode.NOT_FOUND_DATA, "해당 쓰레기 카테고리가 존재하지 않습니다."));
        TrashInfo trashInfo = trashInfoRepository.save(TrashInfo.of(trashInfoRequest, trashCategory));

        return TrashInfoResponse.from(trashInfo);
    }
}
