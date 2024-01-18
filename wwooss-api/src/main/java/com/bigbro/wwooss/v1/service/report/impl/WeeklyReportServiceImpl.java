package com.bigbro.wwooss.v1.service.report.impl;

import com.bigbro.wwooss.v1.dto.WeekInfo;
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
import com.bigbro.wwooss.v1.utils.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyReportServiceImpl implements WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    private final UserRepository userRepository;

    private final DateUtil dateUtil;

    @Transactional(readOnly = true)
    @Override
    public WeeklyReportListResponse getWeeklyReport(String date, Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<WeeklyReport> weeklyReport = weeklyReportRepository.findWeeklyReportByUserAtDate(date, user, pageable);

        List<WeeklyReportResponse> weeklyReportResponseList = weeklyReport.getContent().stream().map((report) -> {
            WeekInfo weekInfo = dateUtil.getCurrentWeekOfMonth(dateUtil.convertToDate(report.getWeeklyDate()));

            // 월요일, 일요일 날짜
            Calendar monday = dateUtil.getDayAtWeekOfMonth(weekInfo.getYear(), weekInfo.getMonth(), weekInfo.getWeekOfMonth(), 2);
            Calendar sunday = dateUtil.getDayAtWeekOfMonth(weekInfo.getYear(), weekInfo.getMonth(), weekInfo.getWeekOfMonth(), 1);

            LocalDateTime fromDate = LocalDateTime.of(monday.get(Calendar.YEAR), monday.get(Calendar.MONTH) + 1, monday.get(Calendar.DATE), 0, 0);
            LocalDateTime toDate = LocalDateTime.of(sunday.get(Calendar.YEAR), sunday.get(Calendar.MONTH) + 1, sunday.get(Calendar.DATE), 0, 0);

            return WeeklyReportResponse.of(report.getWeeklyReportId(), weekInfo, fromDate, toDate);
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
