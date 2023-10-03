package com.bigbro.killbill.v1.service.trash.impl;

import com.bigbro.killbill.v1.domain.entity.trash.TrashInfo;
import com.bigbro.killbill.v1.domain.entity.user.User;
import com.bigbro.killbill.v1.enumType.LoginType;
import com.bigbro.killbill.v1.repository.trash.can.TrashCanContentsRepository;
import com.bigbro.killbill.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.killbill.v1.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class TrashCanContentsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrashInfoRepository trashInfoRepository;

    @Mock
    private TrashCanContentsRepository trashCanContentsRepository;

    @InjectMocks
    private TrashCanContentsServiceImpl trashCanContentsService;

    @Test
    @DisplayName("쓰레기통 내용물 적립")
    void createTrashCanContents() {
        User user = User.builder()
                .userId(1L)
                .loginType(LoginType.GOOGLE)
                .build();

        TrashInfo trashInfo =
                TrashInfo.builder()
                        .trashInfoId(1L)
                        .name("플라스틱")
                        .size(1)
                        .weight(1D)
                        .refund(1)
                        .carbonEmissionPerGram(1D)
                        .build();

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        given(trashInfoRepository.findById(1L)).willReturn(Optional.ofNullable(trashInfo));

        assertThat(userRepository).isNotNull();
        assertThat(trashInfoRepository).isNotNull();
        assertThat(trashInfo.getName()).isEqualTo("플라스틱");

        then(userRepository.findById(1L)).equals(user);
        then(trashInfoRepository.findById(1L)).equals(trashInfo);
    }

}