package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WowReport {

    // 전주 대비 탄소배출량 변화량
    private double wowCarbonEmission;

    // 전주 대비 환급금 변화량
    private long wowRefund;

    // 전주 대비 버린 쓰레기 수 변화량
    private long wowTrashCount;

    public static WowReport of(double wowCarbonEmission, long wowRefund, long wowTrashCount) {
        return WowReport.builder()
                .wowCarbonEmission(wowCarbonEmission)
                .wowRefund(wowRefund)
                .wowTrashCount(wowTrashCount)
                .build();
    }

    public static WowReport zeroReport() {
        return WowReport.builder()
                .wowCarbonEmission(0)
                .wowRefund(0)
                .wowTrashCount(0)
                .build();
    }
}
