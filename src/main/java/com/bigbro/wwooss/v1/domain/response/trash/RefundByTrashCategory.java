package com.bigbro.wwooss.v1.domain.response.trash;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundByTrashCategory {

    // 쓰레기 카테고리
    private String trashCategoryName;

    // 카테고리별 총 환급금
    private Long refund;

    public static RefundByTrashCategory of(String trashCategoryName, Long refund) {
        return RefundByTrashCategory.builder()
                .trashCategoryName(trashCategoryName)
                .refund(refund)
                .build();
    }

}
