package com.bigbro.wwooss.v1.domain.response.trash;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TrashLogListResponse {

    private boolean hasNext;

    private List<TrashLogResponse> trashLogResponseList;

    public static TrashLogListResponse of(boolean hasNext, List<TrashLogResponse> trashLogResponseList) {
        return TrashLogListResponse.builder()
                .hasNext(hasNext)
                .trashLogResponseList(trashLogResponseList)
                .build();
    }

}
