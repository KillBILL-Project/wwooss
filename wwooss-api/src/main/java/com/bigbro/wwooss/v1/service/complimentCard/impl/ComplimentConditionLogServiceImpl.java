package com.bigbro.wwooss.v1.service.complimentCard.impl;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentConditionLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentConditionLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplimentConditionLogServiceImpl implements ComplimentConditionLogService {

    private final ComplimentConditionLogRepository complimentConditionLogRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean createLoginLog(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<ComplimentConditionLog> log = complimentConditionLogRepository.findByUserAndComplimentTypeOrderByCreatedAtDesc(user, ComplimentType.LOGIN); // 최신 로그

        // log 기록 없을 시 바로 저장
        if(log.isEmpty()) {
            complimentConditionLogRepository.save(ComplimentConditionLog.of(user, 1L, ComplimentType.LOGIN));
            return false;
        }

        ComplimentConditionLog latestLog = log.get(0);
        // 로그가 오늘이랑 동일한 날하면 그대로 리턴
        LocalDateTime today = LocalDateTime.now();
        long between = ChronoUnit.DAYS.between(today, latestLog.getCreatedAt());
        if(between == 0L) {
            return false;
        }

        // 지금 로그인 - 최신 로그인 한 날 하루 이상 차이 시 연속 값 1로 저장
        if (between > 1L) {
            complimentConditionLogRepository.save(ComplimentConditionLog.of(user, 1L, ComplimentType.LOGIN));
            return false;
        }

        // 하루 차이 날때 (전날) => 연속 날짜 up하여 저장
        // 일주일 마다 갱신 -> 전날의 요일이 일요일이 아니면 up
        boolean todayIsSunday = today.getDayOfWeek().equals(DayOfWeek.MONDAY);
        long continuity = todayIsSunday ? 1L : latestLog.getContinuity() + 1;

        complimentConditionLogRepository.save(ComplimentConditionLog.of(user, continuity, ComplimentType.LOGIN));
        return continuity == 3L;
    }
}
