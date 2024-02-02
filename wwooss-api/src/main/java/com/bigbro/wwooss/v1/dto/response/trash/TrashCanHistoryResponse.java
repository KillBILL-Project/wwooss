package com.bigbro.wwooss.v1.dto.response.trash;

import com.bigbro.wwooss.v1.entity.trash.can.TrashCanHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistoryResponse {

    private Long trashCanHistoryId;

    private LocalDateTime createdAt;

    private Double carbonSaving;

    private Long refund;

    public static TrashCanHistoryResponse from(TrashCanHistory trashCanHistory) {
        return TrashCanHistoryResponse.builder()
                .trashCanHistoryId(trashCanHistory.getTrashCanHistoryId())
                .carbonSaving(trashCanHistory.getCarbonSaving())
                .refund(trashCanHistory.getRefund())
                .createdAt(trashCanHistory.getCreatedAt())
                .build();
    }
}
