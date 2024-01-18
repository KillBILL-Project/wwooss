package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanInfo {

    private Long trashCanId;

    private Long lng;

    private Long lat;

    private String address;

    private String trashCategory;

    public static TrashCanInfo of(Long trashCanId, Long lng, Long lat, String address, String trashCategory) {
        return TrashCanInfo.builder()
                .trashCanId(trashCanId)
                .lng(lng)
                .lat(lat)
                .address(address)
                .trashCategory(trashCategory)
                .build();
    }
}
