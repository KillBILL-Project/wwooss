package com.bigbro.wwooss.v1.service.report.impl;

import com.bigbro.wwooss.v1.dto.ComplimentCardIcon;
import com.bigbro.wwooss.v1.dto.WeekInfo;
import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportResponse;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardMetaRepository;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardRepository;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.impl.ComplimentCardServiceImpl;
import com.bigbro.wwooss.v1.utils.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
    private ComplimentCardRepository complimentCardRepository;

    @Mock
    private ComplimentCardMetaRepository complimentCardMetaRepository;


    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private WeeklyReportServiceImpl weeklyReportService;

    @InjectMocks
    private ComplimentCardServiceImpl complimentCardService;

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
            WeekInfo weekInfo = WeekInfo.of(2024, 4, 1);


            LocalDateTime fromDate = LocalDateTime.of(2024, 4, 1, 0
                    , 0);
            LocalDateTime toDate = LocalDateTime.of(2024, 4, 8, 0,
                    0);

            // 해당 기간 내 칭찬카드 조회
            List<ComplimentCardResponse> complimentCardList = complimentCardService.getComplimentCardAtDate(user,
                    fromDate, toDate);
            List<ComplimentCardIcon> cardIconList = complimentCardList.stream()
                    .map(card -> ComplimentCardIcon.of(card.getComplimentCardId(), card.getCardImage())).toList();

            return WeeklyReportResponse.of(report.getWeeklyReportId(), weekInfo, fromDate, toDate, cardIconList);
        }).toList();

        assertThat(userRepository).isNotNull();
        then(userRepository.findById(1L)).equals(user);
    }


}
