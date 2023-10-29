package com.bigbro.wwooss.v1.dto.response.trash;

import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TrashLogResponse {

    private Long trashLogId;

    private String trashCategoryName;

    private Integer size;

    private Long trashCount;

    private LocalDateTime createdAt;

    public static TrashLogResponse of(TrashLog trashLog) {
        return TrashLogResponse.builder()
                .trashLogId(trashLog.getTrashLogId())
                .trashCategoryName(trashLog.getTrashInfo().getName())
                .size(trashLog.getSize())
                .trashCount(trashLog.getTrashCount())
                .createdAt(trashLog.getCreatedAt())
                .build();
    }
}
