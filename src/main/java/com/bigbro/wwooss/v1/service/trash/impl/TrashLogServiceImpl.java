package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.response.trash.TrashLogListResponse;
import com.bigbro.wwooss.v1.domain.response.trash.TrashLogResponse;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import com.bigbro.wwooss.v1.util.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashLogServiceImpl implements TrashLogService {

    private final TrashLogRepository trashLogRepository;

    private final UserRepository userRepository;

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

    @Override
    public TrashLogListResponse getTrashLogList(Long userId, LocalDateTime date, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<TrashLog> trashLogList = trashLogRepository.findByUserAndDateBetweenOneMonth(user, date, pageable);

        return TrashLogListResponse.of(trashLogList.hasNext(), trashLogList.getContent().stream().map(TrashLogResponse::of).toList());
    }
}
