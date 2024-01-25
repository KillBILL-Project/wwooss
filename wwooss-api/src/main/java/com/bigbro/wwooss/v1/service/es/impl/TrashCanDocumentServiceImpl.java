package com.bigbro.wwooss.v1.service.es.impl;

import com.bigbro.wwooss.v1.document.TrashCanDocument;
import com.bigbro.wwooss.v1.dto.TrashCanInfoList;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanResponse;
import com.bigbro.wwooss.v1.repository.TrashCanDocumentRepository;
import com.bigbro.wwooss.v1.service.es.TrashCanDocumentService;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanDocumentServiceImpl implements TrashCanDocumentService {

    private final TrashCanService trashCanService;

    private final TrashCanDocumentRepository trashCanDocumentRepository;

    @Override
    @Transactional
    public void migrationTrashCanDocument() {
        boolean next = true;

        Pageable pageable = Pageable.ofSize(10);

        while(next) {
            // es에 저장 안된 값 가져오기
            TrashCanInfoList trashCanInfo = trashCanService.getTrashCanInfo(pageable);
            // es에 저장
            trashCanDocumentRepository.saveTrashCan(trashCanInfo.getTrashCanInfoList());
            // es에 저장된 값 saveCompleted true로 변경
            next = trashCanInfo.getHasNext();
            pageable = pageable.next();
        }
        trashCanService.updateSavedCompleted();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrashCanResponse> findTrashCanByGeoLocation(Double lat, Double lng, Integer distance, String trashType) {
        List<TrashCanDocument> findTrashCanLocation = trashCanDocumentRepository.findByGeoLocationAndTrashType(lat, lng, distance, trashType);
        return findTrashCanLocation.stream().map(trashCan -> {
            String trashTypeListString = trashCan.getTrashType();
            List<String> trashTypeList = Arrays.asList(trashTypeListString.split(","));

            return TrashCanResponse.of(trashCan.getId(),
                    trashCan.getLocation().getLat(),
                    trashCan.getLocation().getLon(),
                    trashCan.getAddress(),
                    trashTypeList);
        }).toList();
    }

}
