package com.bigbro.wwooss.v1.service.report.impl;

import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.dto.WowReport;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportResponse;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WeeklyReportServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeeklyReportRepository weeklyReportRepository;

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
                        .weekNumber(1L)
                        .weeklyTrashCount(3L)
                        .user(user)
                        .build()
        );

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));

        List<WeeklyReportResponse> weeklyReportResponseList = weeklyReportList.stream().map((report) -> {
            LocalDateTime weekNumberDate = user.getCreatedAt().plusWeeks(report.getWeekNumber() - 1);

            // N주차 기간 구하기
            DayOfWeek dayOfWeek = weekNumberDate.getDayOfWeek();
            LocalDateTime fromDate = weekNumberDate.minusDays(dayOfWeek.ordinal());
            LocalDateTime toDate = weekNumberDate.plusDays(( 6 - dayOfWeek.ordinal()));

            return WeeklyReportResponse.of(report.getWeeklyReportId(), report.getWeekNumber(), fromDate, toDate);
        }).toList();

        assertThat(userRepository).isNotNull();
        then(userRepository.findById(1L)).equals(user);

        assertThat(weeklyReportResponseList)
                .extracting("weeklyReportId", "weekNumber", "fromDate", "toDate")
                .contains(
                        tuple(1L, 1L,
                                LocalDateTime.of(2023, 10, 23, 13, 10),
                                LocalDateTime.of(2023, 10, 29, 13, 10))
                );
    }


}
