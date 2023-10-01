package com.bigbro.killbill.v1.service.trash.impl;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfoEntity;
import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.killbill.v1.exception.DataNotFoundException;
import com.bigbro.killbill.v1.repository.trash.category.TrashCategoryRepository;
import com.bigbro.killbill.v1.repository.trash.info.TrashInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
        TrashCategoryEntity trashCategoryEntity = TrashCategoryEntity.builder()
                .trashCategoryId(1L)
                .trashCategoryName("플라스틱")
                .build();

        List<TrashInfoEntity> trashInfoEntityList = List.of(
                TrashInfoEntity.builder()
                        .trashInfoId(1L)
                        .name("플라스틱")
                        .size(1)
                        .weight(1D)
                        .refund(1)
                        .carbonEmissionPerGram(1D)
                        .build()
        );
        List<TrashInfoResponse> trashInfoResponseList = trashInfoEntityList.stream().map(TrashInfoResponse::from).toList();

        given(trashCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(trashCategoryEntity));
        given(trashInfoRepository.findTrashInfoEntitiesByTrashCategoryId(1L)).willReturn(trashInfoEntityList);

        Optional<TrashCategoryEntity> findCategoryEntity = trashCategoryRepository.findById(1L);
        List<TrashInfoEntity> trashInfoEntities = trashInfoRepository.findTrashInfoEntitiesByTrashCategoryId(1L);

        assertThat(findCategoryEntity).isNotNull();

        then(findCategoryEntity).equals(trashCategoryEntity);
        then(trashInfoEntities).equals(trashInfoResponseList);
    }

    @Test
    @DisplayName("쓰레기 정보 목록 불러오기")
    void findTrashInfoListFail() {
        given(trashCategoryRepository.findById(1L)).willReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> trashInfoService.getTrashInfoByCategoryId(1L));

        assertEquals("4000", exception.getCode());
        assertEquals("해당 쓰레기 카테고리가 존재하지 않습니다.", exception.getMessage());
    }

}