package com.bigbro.wwooss.v1.utils;

import com.bigbro.wwooss.v1.dto.ResultOfDiscardedTrash;

public class TrashUtil {
    private static final long WEIGHT_INTERVAL = 3;

    public static ResultOfDiscardedTrash discardResult(Long trashCount,
                                                       Double trashWeight,
                                                       Integer trashSize,
                                                       Double carbonEmissionPerGram,
                                                       Long refundUnit) {
        // size : 0 ~ 10
        // 버린 쓰레기 양 * 버린 쓰레기 사이즈의 무게
        Double amountOfTrash =
                trashCount * ( trashWeight + WEIGHT_INTERVAL * (trashSize));
        double carbonEmission = amountOfTrash * carbonEmissionPerGram;
        long refund = (long) (amountOfTrash * refundUnit);

        return ResultOfDiscardedTrash.of(carbonEmission, refund);
    }

}
