package com.bigbro.wwooss.v1.service.userActiveLog.impl;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.userActiveLog.UserActiveLogResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.userActiveLog.UserActiveLog;
import com.bigbro.wwooss.v1.enumType.ActiveLogType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.repository.userActiveLog.UserActiveLogRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.userActiveLog.UserActiveLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActiveLogServiceImpl implements UserActiveLogService {

    private final UserActiveLogRepository userActiveLogRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Double getActiveLogTotalValue(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        return userActiveLogRepository.selectSumActiveLog(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getVerifiedLogTotalValue(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        return userActiveLogRepository.selectSumVerifiedLog(user);
    }

    @Override
    @Transactional
    public UserActiveLogResponse recordActiveLog(ActiveLogType activeLogType, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        UserActiveLog userActiveLog = UserActiveLog.of(activeLogType, user);

        UserActiveLog savedUserActiveLog = userActiveLogRepository.save(userActiveLog);
        return UserActiveLogResponse.from(savedUserActiveLog);
    }

    @Override
    @Transactional
    public UserActiveLogResponse verifiedLog(Long logId) {
        UserActiveLog userActiveLog = userActiveLogRepository.findById(logId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 활동 로그입니다."));
        userActiveLog.updateVerified(true);
        return UserActiveLogResponse.from(userActiveLog);
    }
}
