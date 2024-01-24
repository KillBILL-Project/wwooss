package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultOfDiscardedTrash {

    private double carbonSaving;

    private long refund;

    public static ResultOfDiscardedTrash of(double carbonSaving, long refund) {
        return ResultOfDiscardedTrash.builder()
                .carbonSaving(carbonSaving)
                .refund(refund)
                .build();
    }
}
