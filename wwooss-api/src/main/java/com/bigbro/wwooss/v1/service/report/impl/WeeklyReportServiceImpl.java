package com.bigbro.wwooss.v1.service.report.impl;

import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportDetailResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportListResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportResponse;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.report.WeeklyReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyReportServiceImpl implements WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public WeeklyReportListResponse getWeeklyReport(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<WeeklyReport> weeklyReport = weeklyReportRepository.findWeeklyReportByUser(user, pageable);

        List<WeeklyReportResponse> weeklyReportResponseList = weeklyReport.getContent().stream().map((report) -> {
            LocalDateTime weekNumberDate = user.getCreatedAt().plusWeeks(report.getWeekNumber());

            // N주차 기간 구하기
            DayOfWeek dayOfWeek = weekNumberDate.getDayOfWeek();
            LocalDateTime fromDate = weekNumberDate.minusDays(dayOfWeek.ordinal());
            LocalDateTime toDate = weekNumberDate.plusDays(( 6 - dayOfWeek.ordinal()));

            return WeeklyReportResponse.of(report.getWeeklyReportId(), report.getWeekNumber(), fromDate, toDate);
        }).toList();

        return WeeklyReportListResponse.of(weeklyReport.hasNext(), weeklyReportResponseList);
    }

    @Transactional(readOnly = true)
    @Override
    public WeeklyReportDetailResponse getWeeklyReportDetail(long weeklyReportId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        WeeklyReport weeklyReportDetail = weeklyReportRepository.findWeeklyReportByWeeklyReportIdAndUser(weeklyReportId, user).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 리프트입니다."));;

        return WeeklyReportDetailResponse.from(weeklyReportDetail);
    }
}
