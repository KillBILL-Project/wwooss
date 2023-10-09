package com.bigbro.wwooss.v1.domain.response.trash;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
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

    public static TrashCanHistoryResponse from(TrashCanHistory trashCanHistory) {
        return TrashCanHistoryResponse.builder()
                .trashCanHistoryId(trashCanHistory.getTrashCanHistoryId())
                .createdAt(trashCanHistory.getCreatedAt())
                .build();
    }
}
