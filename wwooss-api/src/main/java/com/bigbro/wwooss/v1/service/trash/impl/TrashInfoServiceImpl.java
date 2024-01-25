package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.dto.request.trash.info.TrashInfoRequest;
import com.bigbro.wwooss.v1.dto.response.trash.TrashInfoResponse;
import com.bigbro.wwooss.v1.entity.trash.category.TrashCategory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.category.TrashCategoryRepository;
import com.bigbro.wwooss.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.trash.info.TrashInfoService;
import com.bigbro.wwooss.v1.utils.ImageUtil;
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

    private final ImageUtil imageUtil;

    @Override
    @Transactional
    public List<TrashInfoResponse> getTrashInfo() {
        List<TrashInfo> trashMetaInfo = trashInfoRepository.findAll();
        return trashMetaInfo.stream().map((trashInfo) -> TrashInfoResponse.of(trashInfo, imageUtil.baseTrashImagePath())).toList();
    }

    @Override
    @Transactional
    public void createTrashInfo(TrashInfoRequest trashInfoRequest) {
        TrashCategory trashCategory = trashCategoryRepository.findById(trashInfoRequest.getTrashCategoryId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "해당 쓰레기 카테고리가 존재하지 않습니다."));
        trashInfoRepository.save(TrashInfo.of(
                trashInfoRequest.getName(),
                trashInfoRequest.getWeight(),
                trashInfoRequest.getCarbonSaving(),
                trashInfoRequest.getRefund(), trashCategory, trashInfoRequest.getSize())
        );
    }
}
