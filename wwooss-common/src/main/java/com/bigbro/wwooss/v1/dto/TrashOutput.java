package com.bigbro.wwooss.v1.dto;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Builder
public class TrashOutput {
    long totalRefund;
    double totalCarbonSaving;
    HashMap<TrashType, Long> refundByCategory;
    HashMap<TrashType, Double> carbonSavingByCategory;

    public static TrashOutput of(long totalRefund, double totalCarbonSaving, HashMap<TrashType, Long> refundByCategory, HashMap<TrashType, Double> carbonSavingByCategory) {
        return TrashOutput.builder()
                .totalRefund(totalRefund)
                .totalCarbonSaving(totalCarbonSaving)
                .refundByCategory(refundByCategory)
                .carbonSavingByCategory(carbonSavingByCategory)
                .build();
    }
}
