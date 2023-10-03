package com.bigbro.killbill.v1.service.trash.impl;

import com.bigbro.killbill.v1.common.KillBillResponseCode;
import com.bigbro.killbill.v1.domain.entity.trash.TrashCanContents;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfo;
import com.bigbro.killbill.v1.domain.entity.user.User;
import com.bigbro.killbill.v1.domain.request.trash.can.TrashCanContentsRequest;
import com.bigbro.killbill.v1.exception.DataNotFoundException;
import com.bigbro.killbill.v1.repository.trash.can.TrashCanContentsRepository;
import com.bigbro.killbill.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.killbill.v1.repository.user.UserRepository;
import com.bigbro.killbill.v1.service.trash.can.TrashCanContentsService;
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

    @Override
    @Transactional
    public void createTrashCanContents(TrashCanContentsRequest trashCanContentsRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(KillBillResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        TrashInfo trashInfo = trashInfoRepository.findById(trashCanContentsRequest.getTrashInfoId()).orElseThrow(() -> new DataNotFoundException(KillBillResponseCode.NOT_FOUND_DATA, "존재하지 않는 쓰레기 정보 입니다."));

        // TODO : 쓰레기 로그 테이블 생성 후 로그도 저장 로직 추가
        trashCanContentsRepository.save(TrashCanContents.of(trashInfo, user, trashCanContentsRequest.getTrashCount()));
    }
}
