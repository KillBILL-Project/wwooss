package com.bigbro.wwooss.v1.dto;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundByTrashCategory {

    // 쓰레기 카테고리
    private TrashType trashCategoryName;

    // 카테고리별 총 환급금
    private Long refund;

    public static RefundByTrashCategory of(TrashType trashCategoryName, Long refund) {
        return RefundByTrashCategory.builder()
                .trashCategoryName(trashCategoryName)
                .refund(refund)
                .build();
    }

}
