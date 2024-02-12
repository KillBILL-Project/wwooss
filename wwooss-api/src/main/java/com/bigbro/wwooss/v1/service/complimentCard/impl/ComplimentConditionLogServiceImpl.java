package com.bigbro.wwooss.v1.service.complimentCard.impl;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentConditionLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentConditionLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.utils.DateUtil;
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

    private final DateUtil dateUtil;

    private static final Long LOGIN_LOG_COUNT = 3L;
    private static final Long THROW_TRASH_COUNT = 3L;

    private static final Long MIN_LOG = 1L;

    @Override
    @Transactional
    public boolean createLoginLog(long userId) {
        return saveComplimentConditionInADate(userId, ComplimentType.LOGIN, LOGIN_LOG_COUNT);
    }

    @Override
    @Transactional
    public boolean createThrowTrashLog(long userId) {
        return saveComplimentConditionInACount(userId, ComplimentType.THROW_TRASH, THROW_TRASH_COUNT);
    }

    /**
     * 쌓이는 기준이 일주일 동안의 연속 날짜인 경우
     */
    private boolean saveComplimentConditionInADate(Long userId, ComplimentType complimentType, Long continuityCondition) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<ComplimentConditionLog> log = complimentConditionLogRepository.findByUserAndComplimentTypeOrderByCreatedAtDesc(user, complimentType); // 최신 로그

        // log 기록 없을 시 바로 저장
        if(log.isEmpty()) {
            complimentConditionLogRepository.save(ComplimentConditionLog.of(user, MIN_LOG, complimentType));
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
            complimentConditionLogRepository.save(ComplimentConditionLog.of(user, MIN_LOG, complimentType));
            return false;
        }

        // 월요일마다 갱신 => 1로 저장
        boolean todayIsMonday = today.getDayOfWeek().equals(DayOfWeek.MONDAY);
        long continuity = todayIsMonday ? MIN_LOG : latestLog.getContinuity() + 1;

        complimentConditionLogRepository.save(ComplimentConditionLog.of(user, continuity, complimentType));
        return continuity == continuityCondition;
    }

    /**
     * 쌓이는 기준이 일주일 동안의 횟수인 경우
     */
    private boolean saveComplimentConditionInACount(Long userId, ComplimentType complimentType, Long continuityCondition) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<ComplimentConditionLog> log = complimentConditionLogRepository.findByUserAndComplimentTypeOrderByCreatedAtDesc(user, complimentType); // 최신 로그

        // log 기록 없을 시 바로 저장
        if(log.isEmpty()) {
            complimentConditionLogRepository.save(ComplimentConditionLog.of(user, MIN_LOG, complimentType));
            return false;
        }

        ComplimentConditionLog latestLog = log.get(0);
        LocalDateTime today = LocalDateTime.now();

        // 최신 로그가 이번주에 포함되면 +1 아니면 1
        long continuity = dateUtil.isIncludeThisWeek(today, latestLog.getCreatedAt()) ? latestLog.getContinuity() + 1 : MIN_LOG;

        complimentConditionLogRepository.save(ComplimentConditionLog.of(user, continuity, complimentType));
        return continuity == continuityCondition;
    }
}
