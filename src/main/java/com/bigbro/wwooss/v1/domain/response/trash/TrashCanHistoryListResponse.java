package com.bigbro.wwooss.v1.domain.response.trash;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistoryListResponse {

    private boolean hasNext;

    private List<TrashCanHistoryResponse> trashCanHistoryResponseList;

    public static TrashCanHistoryListResponse of(boolean hasNext, List<TrashCanHistoryResponse> trashCanHistoryResponseList) {
        return TrashCanHistoryListResponse.builder()
                .hasNext(hasNext)
                .trashCanHistoryResponseList(trashCanHistoryResponseList)
                .build();
    }
}
