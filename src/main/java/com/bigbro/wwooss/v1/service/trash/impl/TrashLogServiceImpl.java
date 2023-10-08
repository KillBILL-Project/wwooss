package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashLogServiceImpl implements TrashLogService {

    private final TrashLogRepository trashLogRepository;

    @Override
    @Transactional
    public void createTrashLog(User user, TrashInfo trashInfo, Long trashCount, Integer size) {
        trashLogRepository.save(TrashLog.of(user, trashInfo, trashCount, size));
    }

    @Override
    public void updateTrashLogTrashHistory(TrashCanHistory trashCanHistory, User user) {
        List<TrashLog> trashLogList = trashLogRepository.findAllByUserAndTrashCanHistoryNull(user);
        trashLogList.forEach((trashLog -> trashLog.updateTrashHistory(trashCanHistory)));
    }
}
