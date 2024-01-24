package com.bigbro.wwooss.v1.dto;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbonSavingByTrashCategory {

    // 쓰레기 카테고리
    private TrashType trashCategoryName;

    // 카테고리별 총 탄소배출량
    private Double carbonEmission;

    public static CarbonSavingByTrashCategory of(TrashType trashCategoryName, Double carbonEmission) {
        return CarbonSavingByTrashCategory.builder()
                .trashCategoryName(trashCategoryName)
                .carbonEmission(carbonEmission)
                .build();
    }

}
