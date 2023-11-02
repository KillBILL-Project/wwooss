package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultOfDiscardedTrash {

    private double carbonEmission;

    private long refund;

    public static ResultOfDiscardedTrash of(double carbonEmission, long refund) {
        return ResultOfDiscardedTrash.builder()
                .carbonEmission(carbonEmission)
                .refund(refund)
                .build();
    }
}
