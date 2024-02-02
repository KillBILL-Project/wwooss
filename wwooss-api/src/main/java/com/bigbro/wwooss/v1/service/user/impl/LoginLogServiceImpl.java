package com.bigbro.wwooss.v1.service.user.impl;

import com.bigbro.wwooss.v1.entity.user.LoginLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.LoginLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.user.LoginLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLogServiceImpl implements LoginLogService {
    private final LoginLogRepository loginLogRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createLoginLog(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "일치하는 유저정보가 없습니다."));
        LoginLog log = loginLogRepository.findByUserOrderByCreatedAtDesc(user); // 최신 로그

        // log 기록 없을 시 바로 저장
        if(Objects.isNull(log)) {
            loginLogRepository.save(LoginLog.of(user, 1L));
            return;
        }

        // 로그가 오늘이랑 동일한 날하면 그대로 리턴
        LocalDateTime today = LocalDateTime.now();
        long between = ChronoUnit.DAYS.between(today, log.getCreatedAt());
        if(between == 0L) {
            return;
        }

        // 지금 로그인 날 - 최신 로그인 한 날 하루 이상 차이 시 연속 값 1로 저장
        if (between > 1L) {
            loginLogRepository.save(LoginLog.of(user, 1L));
            return;
        }

        // 하루 차이 날때 (전날) => 연속 날짜 up하여 저장
        // 일주일 마다 갱신 -> 전날의 요일이 일요일이 아니면 up
        boolean todayIsSunday = today.getDayOfWeek().equals(DayOfWeek.MONDAY);
        long continuity = todayIsSunday ? 1L : log.getContinuity() + 1;

        loginLogRepository.save(LoginLog.of(user, continuity));
    }
}
