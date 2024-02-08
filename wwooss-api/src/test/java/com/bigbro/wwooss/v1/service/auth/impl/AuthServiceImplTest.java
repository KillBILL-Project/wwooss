package com.bigbro.wwooss.v1.service.auth.impl;


import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.entity.user.WithdrawalUser;
import com.bigbro.wwooss.v1.enumType.Gender;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.enumType.UserRole;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.alarm.AlarmRepository;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentCardRepository;
import com.bigbro.wwooss.v1.repository.complimentCard.ComplimentConditionLogRepository;
import com.bigbro.wwooss.v1.repository.notification.NotificationRepository;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanContentsRepository;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanHistoryRepository;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.repository.user.WithdrawalUserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WithdrawalUserRepository withdrawalUserRepository;

    @Mock
    private AlarmRepository alarmRepository;

    @Mock
    private ComplimentCardRepository complimentCardRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ComplimentConditionLogRepository complimentConditionLogRepository;

    @Mock
    private TrashCanContentsRepository trashCanContentsRepository;

    @Mock
    private TrashLogRepository trashLogRepository;

    @Mock
    private TrashCanHistoryRepository trashCanHistoryRepository;

    @Mock
    private WeeklyReportRepository weeklyReportRepository;
    
    @BeforeEach
    void init() {
        User user = User.builder()
                .userId(1L)
                .age(1)
                .email("tkddnjs2312@")
                .country("korea")
                .region("seoul")
                .loginType(LoginType.EMAIL)
                .gender(Gender.M)
                .refreshToken("refresh")
                .userRole(UserRole.USER)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user)).thenReturn(null);

        given(withdrawalUserRepository.findById(1L)).willReturn(
                Optional.ofNullable(WithdrawalUser.builder()
                        .userId(user.getUserId())
                        .userRole(user.getUserRole())
                        .gender(user.getGender())
                        .age(user.getAge())
                        .loginType(user.getLoginType())
                        .country(user.getCountry())
                        .region(user.getRegion())
                        .build()));
    }

    @Test
    @DisplayName("회원탈퇴")
    void withdrawalUser() {
        User findUser = userRepository.findById(1L).orElse(null);
        assertThat(findUser).isNotNull();

        // 알람
        alarmRepository.deleteByUser(findUser);
        assertThat(alarmRepository.findAlarmByUser(findUser)).isEmpty();

        // 알림
        notificationRepository.deleteByUser(findUser);
        assertThat(notificationRepository.findByUser(findUser)).isEmpty();

        // 칭찬 카드
        complimentCardRepository.deleteByUser(findUser);
        assertThat(complimentCardRepository.findByUser(findUser)).isEmpty();

        // 칭찬 카드 로그
        complimentConditionLogRepository.deleteByUser(findUser);
        assertThat(complimentConditionLogRepository.findByUser(findUser)).isEmpty();

        // 쓰레기 버린 이력
        trashLogRepository.deleteByUser(findUser);
        assertThat(trashLogRepository.findByUser(findUser)).isEmpty();

        // 쓰레기 내용물
        trashCanContentsRepository.deleteByUser(findUser);
        assertThat(trashCanContentsRepository.findAllByUser(findUser)).isEmpty();

        // 쓰레기 비운 이력
        trashCanHistoryRepository.deleteByUser(findUser);
        assertThat(trashCanHistoryRepository.findAllByUser(findUser)).isEmpty();

        // 주간 리포트
        weeklyReportRepository.deleteByUser(findUser);
        assertThat(weeklyReportRepository.findAllByUser(findUser)).isEmpty();

        WithdrawalUser withdrawalUser = WithdrawalUser.of(
                findUser.getUserId(),
                findUser.getUserRole(),
                findUser.getGender(),
                findUser.getAge(),
                findUser.getLoginType(),
                findUser.getCountry(),
                findUser.getRegion()
        );
        withdrawalUserRepository.save(withdrawalUser);

        WithdrawalUser withdrawalResult = withdrawalUserRepository.findById(1L).orElse(null);
        assertThat(withdrawalResult).isNotNull();


        userRepository.delete(findUser);
        assertThat(userRepository.findById(1L)).isNull();
    }

}
