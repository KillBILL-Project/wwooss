package com.bigbro.wwooss.v1.dto.response.trash;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TrashLogListResponse {

    private boolean hasNext;

    private long totalCount;

    private List<TrashLogResponse> trashLogResponseList;

    public static TrashLogListResponse of(boolean hasNext, long totalCount, List<TrashLogResponse> trashLogResponseList) {
        return TrashLogListResponse.builder()
                .hasNext(hasNext)
                .totalCount(totalCount)
                .trashLogResponseList(trashLogResponseList)
                .build();
    }

}
