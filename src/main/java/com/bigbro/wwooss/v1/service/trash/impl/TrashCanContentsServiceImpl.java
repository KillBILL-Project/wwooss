package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.domain.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.domain.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanContentsRepository;
import com.bigbro.wwooss.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanContentsService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanContentsServiceImpl implements TrashCanContentsService {

    private final UserRepository userRepository;

    private final TrashInfoRepository trashInfoRepository;

    private final TrashCanContentsRepository trashCanContentsRepository;

    private final TrashLogService trashLogService;

    @Override
    @Transactional
    public void createTrashCanContents(TrashCanContentsRequest trashCanContentsRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        TrashInfo trashInfo = trashInfoRepository.findById(trashCanContentsRequest.getTrashInfoId()).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 쓰레기 정보 입니다."));

        trashCanContentsRepository.save(TrashCanContents.of(trashInfo, user, trashCanContentsRequest.getTrashCount(), trashCanContentsRequest.getSize()));
        trashLogService.createTrashLog(user, trashInfo, trashCanContentsRequest.getTrashCount(), trashCanContentsRequest.getSize());
    }
}
