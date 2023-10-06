package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.common.WwoossResponseCode;
import com.bigbro.wwooss.v1.domain.entity.trash.TrashCategory;
import com.bigbro.wwooss.v1.domain.entity.trash.TrashInfo;
import com.bigbro.wwooss.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.category.TrashCategoryRepository;
import com.bigbro.wwooss.v1.repository.trash.info.TrashInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
public class TrashInfoServiceImplTest {

    @Mock
    private TrashInfoRepository trashInfoRepository;

    @Mock
    private TrashCategoryRepository trashCategoryRepository;

    @InjectMocks
    private TrashInfoServiceImpl trashInfoService;


    @Test
    @DisplayName("쓰레기 정보 목록 불러오기")
    void findTrashInfoList() {
        TrashCategory trashCategory = TrashCategory.builder()
                .trashCategoryId(1L)
                .trashCategoryName("플라스틱")
                .build();

        TrashInfo plastic = TrashInfo.builder()
                .trashInfoId(1L)
                .name("플라스틱")
                .size(1)
                .weight(1D)
                .refund(1)
                .carbonEmissionPerGram(1D)
                .build();
        TrashInfo can = TrashInfo.builder()
                .trashInfoId(2L)
                .name("캔")
                .size(2)
                .weight(3D)
                .refund(10)
                .carbonEmissionPerGram(2D)
                .build();

        List<TrashInfo> trashInfoList = List.of(plastic, can);

        List<TrashInfoResponse> trashInfoResponseList = trashInfoList.stream().map(TrashInfoResponse::from).toList();

        given(trashCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(trashCategory));
        given(trashInfoRepository.findTrashInfoEntitiesByTrashCategory(trashCategory)).willReturn(trashInfoList);

        Optional<TrashCategory> findCategoryEntity = trashCategoryRepository.findById(1L);
        List<TrashInfo> trashInfoEntities = trashInfoRepository.findTrashInfoEntitiesByTrashCategory(trashCategory);

        assertThat(findCategoryEntity).isNotNull();
        assertThat(trashInfoEntities)
                .filteredOn("name", "캔")
                .containsOnly(can);
        assertThat(trashInfoEntities)
                .extracting("name", "refund")
                .contains(
                        tuple("플라스틱", 1),
                        tuple("캔", 10)
                );

        then(findCategoryEntity).equals(trashCategory);
        then(trashInfoEntities).equals(trashInfoResponseList);
    }

    @Test
    @DisplayName("쓰레기 정보 목록 불러오기")
    void findTrashInfoListFail() {
        given(trashCategoryRepository.findById(1L)).willReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> trashInfoService.getTrashInfoByCategoryId(1L));

        assertEquals(WwoossResponseCode.NOT_FOUND_DATA.getCode(), exception.getCode());
        assertEquals("해당 쓰레기 카테고리가 존재하지 않습니다.", exception.getMessage());
    }

}