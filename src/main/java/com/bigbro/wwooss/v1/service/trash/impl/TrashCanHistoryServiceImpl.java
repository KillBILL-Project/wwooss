package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanHistoryRepository;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistoryServiceImpl implements TrashCanHistoryService {

    private final TrashCanHistoryRepository trashCanHistoryRepository;

    private final TrashLogService trashLogService;

    @Override
    @Transactional
    public void createTrashCanHistory(List<TrashCanContents> trashCanContentsList, User user) {
        long totalRefund = 0L;
        double totalCarbonEmission = 0D;

        for (TrashCanContents trashCanContents : trashCanContentsList) {
            // size : 0 ~ 100 (10단위)
            // 표준 쓰레기 탄소량 기준 10
            Long amountOfTrash = trashCanContents.getTrashCount() * (trashCanContents.getSize() / 10);

            totalCarbonEmission += amountOfTrash * trashCanContents.getTrashInfo().getStandardCarbonEmission();
            totalRefund += amountOfTrash * trashCanContents.getTrashInfo().getRefund();
        }

        TrashCanHistory savedTrashCanHistory = trashCanHistoryRepository.save(TrashCanHistory.of(totalCarbonEmission, totalRefund, user));
        trashLogService.updateTrashLogTrashHistory(savedTrashCanHistory, user);
    }
}
