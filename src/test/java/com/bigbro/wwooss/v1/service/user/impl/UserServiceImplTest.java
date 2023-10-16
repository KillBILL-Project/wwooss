package com.bigbro.wwooss.v1.service.user.impl;

import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("유저 푸쉬 알림 업데이트")
    void updatePushConsent() {
        User user = User.builder()
                .userId(1L)
                .loginType(LoginType.GOOGLE)
                .pushConsent(false)
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));

        User findUser = userRepository.findById(1L).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        assertThat(findUser).isNotNull();

        findUser.updatePushConsent(true);

        assertTrue(findUser.isPushConsent());
    }
}
