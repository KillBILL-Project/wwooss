package com.bigbro.wwooss.v1.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanInfoList {

    private Boolean hasNext;

    private List<TrashCanInfo> trashCanInfoList;

    public static TrashCanInfoList of(boolean hasNext, List<TrashCanInfo> trashCanInfoList) {
        return TrashCanInfoList.builder()
                .hasNext(hasNext)
                .trashCanInfoList(trashCanInfoList)
                .build();
    }
}
