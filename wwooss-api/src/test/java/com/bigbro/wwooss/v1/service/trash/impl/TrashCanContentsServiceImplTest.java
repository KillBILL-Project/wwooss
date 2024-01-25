package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.entity.trash.can.TrashCanContents;
import com.bigbro.wwooss.v1.entity.trash.category.TrashCategory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.LoginType;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.can.TrashCanContentsRepository;
import com.bigbro.wwooss.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TrashCanContentsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrashInfoRepository trashInfoRepository;

    @Mock
    private TrashCanContentsRepository trashCanContentsRepository;

    @Mock
    private TrashLogService trashLogService;

    @Mock
    private TrashCanHistoryService trashCanHistoryService;

    @InjectMocks
    private TrashCanContentsServiceImpl trashCanContentsService;

    @Test
    @DisplayName("쓰레기통 내용물 적립(버리기)")
    void createTrashCanContents() {
        User user = User.builder()
                .userId(1L)
                .loginType(LoginType.GOOGLE)
                .build();
        TrashCategory trashCategory = TrashCategory.builder()
                .trashCategoryId(1L)
                .trashType(TrashType.PLASTIC)
                .build();

        TrashInfo trashInfo =
                TrashInfo.builder()
                        .trashInfoId(1L)
                        .name("plastic_01")
                        .weight(1D)
                        .refund(1L)
                        .carbonSaving(1D)
                        .trashImagePath("image/trash")
                        .trashCategory(trashCategory)
                        .build();

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        given(trashInfoRepository.findById(1L)).willReturn(Optional.ofNullable(trashInfo));

        assertThat(userRepository).isNotNull();
        assertThat(trashInfoRepository).isNotNull();
        assertThat(trashInfo.getName()).isEqualTo("plastic_01");
        assertThat(trashInfo.getTrashCategory().getTrashType()).isEqualTo(TrashType.PLASTIC);

        then(userRepository.findById(1L)).equals(user);
        then(trashInfoRepository.findById(1L)).equals(trashInfo);
    }

    @Test
    @DisplayName("쓰레기통 비우기 시 유저 없을 때")
    void notFoundUser() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> trashCanContentsService.deleteTrashCanContents(1L));

        assertEquals(WwoossResponseCode.NOT_FOUND_DATA.getCode(), exception.getCode());
        assertEquals("존재하지 않는 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쓰레기통 비우기")
    void deleteTrashCan() {
        User user = User.builder()
                .userId(1L)
                .loginType(LoginType.GOOGLE)
                .build();
        List<TrashCanContents> trashCanContentsList = List.of(TrashCanContents.builder()
                .trashCanContentsId(1L)
                .user(user)
                .build());
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        given(trashCanContentsRepository.findAllByUser(user)).willReturn(trashCanContentsList);

        trashCanContentsService.deleteTrashCanContents(1L);

        verify(trashCanContentsRepository, times(1)).deleteAll(trashCanContentsList);
    }

}
