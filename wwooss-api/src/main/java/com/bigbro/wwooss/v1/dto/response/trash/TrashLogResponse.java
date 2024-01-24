package com.bigbro.wwooss.v1.dto.response.trash;

import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.enumType.TrashSize;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TrashLogResponse {

    private Long trashLogId;

    private String trashCategoryName;

    private TrashSize size;

    private String trashImagePath;

    private LocalDateTime createdAt;

    public static TrashLogResponse of(TrashLog trashLog) {
        return TrashLogResponse.builder()
                .trashLogId(trashLog.getTrashLogId())
                .trashImagePath(trashLog.getTrashInfo().getTrashImagePath())
                .trashCategoryName(trashLog.getTrashInfo().getName())
                .createdAt(trashLog.getCreatedAt())
                .build();
    }
}
