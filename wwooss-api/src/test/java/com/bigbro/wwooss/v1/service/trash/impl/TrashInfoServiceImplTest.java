package com.bigbro.wwooss.v1.service.trash.impl;

import com.bigbro.wwooss.v1.dto.response.trash.TrashInfoResponse;
import com.bigbro.wwooss.v1.entity.trash.category.TrashCategory;
import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.enumType.TrashSize;
import com.bigbro.wwooss.v1.enumType.TrashType;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.trash.category.TrashCategoryRepository;
import com.bigbro.wwooss.v1.repository.trash.info.TrashInfoRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        TrashCategory plasticCrashCategory = TrashCategory.builder()
                .trashCategoryId(1L)
                .trashType(TrashType.PLASTIC)
                .build();

        TrashInfo plastic_01 = TrashInfo.builder()
                .trashInfoId(1L)
                .name("플라스틱_01")
                .weight(1D)
                .size(TrashSize.MEDIUM)
                .refund(1L)
                .carbonSaving(1D)
                .trashImagePath("image/trash")
                .trashCategory(plasticCrashCategory)
                .build();
        TrashInfo plastic_02 = TrashInfo.builder()
                .trashInfoId(2L)
                .name("플라스틱_02")
                .weight(3D)
                .size(TrashSize.BIG)
                .carbonSaving(10D)
                .trashImagePath("image/trash")
                .refund(10L)
                .trashCategory(plasticCrashCategory)
                .build();

        List<TrashInfo> trashInfoList = List.of(plastic_01, plastic_02);

        List<TrashInfoResponse> trashInfoResponseList = trashInfoList.stream().map(TrashInfoResponse::from).toList();

        given(trashCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(plasticCrashCategory));
        given(trashInfoRepository.findTrashInfoEntitiesByTrashCategory(plasticCrashCategory)).willReturn(trashInfoList);

        Optional<TrashCategory> findCategoryEntity = trashCategoryRepository.findById(1L);
        List<TrashInfo> trashInfoEntities = trashInfoRepository.findTrashInfoEntitiesByTrashCategory(plasticCrashCategory);

        assertThat(findCategoryEntity).isNotNull();
        assertThat(trashInfoEntities)
                .filteredOn("name", "플라스틱_01")
                .containsOnly(plastic_01);
        assertThat(trashInfoEntities)
                .extracting("name", "refund")
                .contains(
                        tuple("플라스틱_01", 1L),
                        tuple("플라스틱_02", 10L)
                );

        then(findCategoryEntity).equals(plasticCrashCategory);
        then(trashInfoEntities).equals(trashInfoResponseList);
    }

    @Test
    @DisplayName("쓰레기 정보 목록 불러오기 실패")
    void findTrashInfoListFail() {
        given(trashCategoryRepository.findById(1L)).willReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> trashInfoService.getTrashInfoByCategoryId(1L));

        assertEquals(WwoossResponseCode.NOT_FOUND_DATA.getCode(), exception.getCode());
        assertEquals("해당 쓰레기 카테고리가 존재하지 않습니다.", exception.getMessage());
    }

}
