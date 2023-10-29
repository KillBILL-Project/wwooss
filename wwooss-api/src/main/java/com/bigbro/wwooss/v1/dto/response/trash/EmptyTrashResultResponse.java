package com.bigbro.wwooss.v1.dto.response.trash;

import com.bigbro.wwooss.v1.dto.CarbonEmissionByTrashCategory;
import com.bigbro.wwooss.v1.dto.RefundByTrashCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmptyTrashResultResponse {

    // 탄소배출량 총합
    private Double totalCarbonEmission;

    // 물건 성분별 탄소 배출
    private List<CarbonEmissionByTrashCategory> carbonEmissionByTrashCategoryList;

    // 환급금 총합
    private Long totalRefund;

    // 물건 성분별 예상 환급금
    private List<RefundByTrashCategory> refundByTrashCategoryList;

    public static EmptyTrashResultResponse of(Double totalCarbonEmission,
                                              List<CarbonEmissionByTrashCategory> carbonEmissionByTrashCategoryList,
                                              Long totalRefund,
                                              List<RefundByTrashCategory> refundByTrashCategoryList) {
        return EmptyTrashResultResponse.builder()
                .totalCarbonEmission(totalCarbonEmission)
                .carbonEmissionByTrashCategoryList(carbonEmissionByTrashCategoryList)
                .totalRefund(totalRefund)
                .refundByTrashCategoryList(refundByTrashCategoryList)
                .build();
    }
}
