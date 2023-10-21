package com.bigbro.wwooss.v1.domain.response.trash;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbonEmissionByTrashCategory {

    // 쓰레기 카테고리
    private String trashCategoryName;

    // 카테고리별 총 탄소배출량
    private Double carbonEmission;

    public static CarbonEmissionByTrashCategory of(String trashCategoryName, Double carbonEmission) {
        return CarbonEmissionByTrashCategory.builder()
                .trashCategoryName(trashCategoryName)
                .carbonEmission(carbonEmission)
                .build();
    }

}
