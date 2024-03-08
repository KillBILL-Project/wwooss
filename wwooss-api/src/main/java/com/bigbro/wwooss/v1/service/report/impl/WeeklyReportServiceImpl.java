package com.bigbro.wwooss.v1.service.report.impl;

import com.bigbro.wwooss.v1.dto.ComplimentCardIcon;
import com.bigbro.wwooss.v1.dto.WeekInfo;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportDetailResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportListResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportResponse;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
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

    private final ComplimentCardService complimentCardService;

    private static final int MONDAY = 2;
    private static final int SUNDAY = 1;

    @Transactional(readOnly = true)
    @Override
    public WeeklyReportListResponse getWeeklyReport(String date, Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Slice<WeeklyReport> weeklyReport = weeklyReportRepository.findWeeklyReportByUserAtDate(date, user, pageable);

        List<WeeklyReportResponse> weeklyReportResponseList = weeklyReport.getContent().stream().map((report) -> {
            WeekInfo weekInfo = dateUtil.getCurrentWeekOfMonth(dateUtil.convertToDate(report.getWeeklyDate()));

            LocalDateTime fromDate = getDateByReportDate(weekInfo, MONDAY);
            LocalDateTime toDate = getDateByReportDate(weekInfo, SUNDAY);

            return WeeklyReportResponse.of(report.getWeeklyReportId(), weekInfo, fromDate, toDate);
        }).toList();

        return WeeklyReportListResponse.of(weeklyReport.hasNext(), weeklyReportResponseList);
    }

    private LocalDateTime getDateByReportDate(WeekInfo weekInfo, int dayOfWeek) {
        Calendar calendar = dateUtil.getDayAtWeekOfMonth(weekInfo.getYear(), weekInfo.getMonth(), weekInfo.getWeekOfMonth(), dayOfWeek);

        switch (dayOfWeek) {
            case MONDAY ->
            {
                return LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), 0, 0);
            }
            case SUNDAY ->
            {
                return LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), 23, 59);
            }
            default -> {
                return null;
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public WeeklyReportDetailResponse getWeeklyReportDetail(Long weeklyReportId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        WeeklyReport weeklyReportDetail = weeklyReportRepository.findWeeklyReportByWeeklyReportIdAndUser(weeklyReportId, user).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 리프트입니다."));;
        WeekInfo weekInfo = dateUtil.getCurrentWeekOfMonth(dateUtil.convertToDate(weeklyReportDetail.getWeeklyDate()));

        LocalDateTime fromDate = getDateByReportDate(weekInfo, MONDAY);
        LocalDateTime toDate = getDateByReportDate(weekInfo, SUNDAY);

        // 해당 기간 내 칭찬카드 조회
        List<ComplimentCardResponse> complimentCardList = complimentCardService.getComplimentCardAtDate(user,
                fromDate, toDate);
        List<ComplimentCardIcon> cardIconList = complimentCardList.stream()
                .map(card -> ComplimentCardIcon.of(card.getComplimentCardId(), card.getCardImage(), card.getTitle())).toList();

        return WeeklyReportDetailResponse.of(weeklyReportDetail, cardIconList);
    }
}
