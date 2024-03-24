package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.dto.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanContentsRepository;
import com.bigbro.wwooss.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanContentsService;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class TrashCanContentsServiceImpl implements TrashCanContentsService {

    private final UserRepository userRepository;

    private final TrashInfoRepository trashInfoRepository;

    private final TrashCanContentsRepository trashCanContentsRepository;

    private final TrashLogService trashLogService;

    private final TrashCanHistoryService trashCanHistoryService;

    @Override
    @Transactional
    public User createTrashCanContents(TrashCanContentsRequest trashCanContentsRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        TrashInfo trashInfo = trashInfoRepository.findById(trashCanContentsRequest.getTrashInfoId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 쓰레기 정보 입니다."));

        trashCanContentsRepository.save(TrashCanContents.of(trashInfo, user));
        trashLogService.createTrashLog(user, trashInfo);
        return user;
    }

    @Override
    @Transactional
    public EmptyTrashResultResponse deleteTrashCanContents(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<TrashCanContents> trashCanContentsList = trashCanContentsRepository.findAllByUser(user);
        if(trashCanContentsList.isEmpty()) {
            log.info("버려진 쓰레기가 존재하지 않습니다.");
            return null;
        };

        // 쓰리기 비우기 전 히스토리 테이블 적재
        EmptyTrashResultResponse emptyTrashResultResponse = trashCanHistoryService.createTrashCanHistory(trashCanContentsList, user);
        trashCanContentsRepository.deleteAll(trashCanContentsList);

        return emptyTrashResultResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTrashCanContentsCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        return trashCanContentsRepository.countByUser(user);
    }

}
