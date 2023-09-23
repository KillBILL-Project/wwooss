package com.bigbro.killbill.v1.domain.response.trash;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryWithInfoResponse {
    private Long trashCategoryId;

    private List<TrashInfoResponse> trashInfoResponseList;

    public static TrashCategoryWithInfoResponse from(Long trashCategoryId, List<TrashInfoResponse> trashInfoResponseList) {
        return TrashCategoryWithInfoResponse.builder()
                .trashCategoryId(trashCategoryId)
                .trashInfoResponseList(trashInfoResponseList)
                .build();
    }
}
