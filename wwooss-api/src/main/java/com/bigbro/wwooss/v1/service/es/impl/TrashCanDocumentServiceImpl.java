package com.bigbro.wwooss.v1.service.es.impl;

import com.bigbro.wwooss.v1.dto.TrashCanInfoList;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanResponse;
import com.bigbro.wwooss.v1.repository.TrashCanDocumentRepository;
import com.bigbro.wwooss.v1.service.es.TrashCanDocumentService;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanDocumentServiceImpl implements TrashCanDocumentService {

    private final TrashCanService trashCanService;

    private final TrashCanDocumentRepository trashCanDocumentRepository;

    @Override
    @Transactional(readOnly = true)
    public void migrationTrashCanDocument() {
        int index = 0;
        boolean next = true;

        while(next) {
            TrashCanInfoList trashCanInfo = trashCanService.getTrashCanInfo(PageRequest.of(index, 10));
            trashCanDocumentRepository.saveTrashCan(trashCanInfo.getTrashCanInfoList());
            next = trashCanInfo.getHasNext();
            ++index;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrashCanResponse> getTrashCanAroundMe(Double lat, Double lng, String trashType) {
        trashCanDocumentRepository.findTrashCanDocumentByTrashType(trashType);
        return null;
    }

}
