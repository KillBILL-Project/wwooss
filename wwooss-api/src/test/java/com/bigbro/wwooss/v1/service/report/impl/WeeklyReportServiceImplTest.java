package com.bigbro.wwooss.v1.service.report.impl;

import com.bigbro.wwooss.v1.dto.WeekInfo;
import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.dto.WowReport;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportResponse;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.utils.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WeeklyReportServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeeklyReportRepository weeklyReportRepository;

    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private WeeklyReportServiceImpl weeklyReportService;

    @Test
    @DisplayName("주간 리포트 가져오기")
    void getWeeklyReport() {
        User user = User.builder()
                .userId(1L)
                .loginType(LoginType.GOOGLE)
                .createdAt(LocalDateTime.of(2023, 10, 24, 13, 10))
                .build();

        List<WeeklyReport> weeklyReportList = List.of(
                WeeklyReport.builder()
                        .weeklyReportId(1L)
                        .attendanceRecord(List.of(1,2,3))
                        .weeklyTrashCountByCategoryList(List.of(WeeklyTrashByCategory.of("플라스틱", 2L)))
                        .weeklyCarbonEmission(3.D)
                        .weeklyRefund(200L)
                        .weeklyTrashCount(3L)
                        .weeklyDate(LocalDateTime.of(2024, 4, 1, 0, 0))
                        .user(user)
                        .build()
        );

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));

        List<WeeklyReportResponse> weeklyReportResponseList = weeklyReportList.stream().map((report) -> {
            WeekInfo weekInfo = dateUtil.getCurrentWeekOfMonth(dateUtil.convertToDate(report.getWeeklyDate()));

            // 월요일, 일요일 날짜
            Calendar monday = dateUtil.getDayAtWeekOfMonth(weekInfo.getYear(), weekInfo.getMonth(), weekInfo.getWeekOfMonth(), 2);
            Calendar sunday = dateUtil.getDayAtWeekOfMonth(weekInfo.getYear(), weekInfo.getMonth(), weekInfo.getWeekOfMonth(), 1);

            LocalDateTime fromDate = LocalDateTime.of(monday.get(Calendar.YEAR), monday.get(Calendar.MONTH) + 1, monday.get(Calendar.DATE), 0, 0);
            LocalDateTime toDate = LocalDateTime.of(sunday.get(Calendar.YEAR), sunday.get(Calendar.MONTH) + 1, sunday.get(Calendar.DATE), 0, 0);

            return WeeklyReportResponse.of(report.getWeeklyReportId(), weekInfo, fromDate, toDate);
        }).toList();

        assertThat(userRepository).isNotNull();
        then(userRepository.findById(1L)).equals(user);

        WeekInfo weekInfo = WeekInfo.of(2024, 4, 1);
        assertThat(weeklyReportResponseList)
                .extracting("weeklyReportId", "weekInfo", "fromDate", "toDate")
                .contains(
                        tuple(1L, weekInfo,
                                LocalDateTime.of(2024, 4, 1, 0, 0),
                                LocalDateTime.of(2024, 4, 7, 0, 0))
                );
    }


}
