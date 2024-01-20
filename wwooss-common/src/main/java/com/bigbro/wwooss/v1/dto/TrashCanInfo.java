package com.bigbro.wwooss.v1.dto;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanInfo {

    private Long trashCanId;

    private Long lng;

    private Long lat;

    private String address;

    private TrashType trashType;

    public static TrashCanInfo of(Long trashCanId, Long lng, Long lat, String address, TrashType trashType) {
        return TrashCanInfo.builder()
                .trashCanId(trashCanId)
                .lng(lng)
                .lat(lat)
                .address(address)
                .trashType(trashType)
                .build();
    }
}
