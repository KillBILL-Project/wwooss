package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TrashCanInfo {

    private Long trashCanId;

    private Double lng;

    private Double lat;

    private String address;

    private String locationName;

    // , 로 구분 => PAPER[종이] / CAN[캔] / PLASTIC[플라스틱] / PET[페트병] / GLASS[병] / VINYL[비닐] / COMMON[기타]
    private String trashType;

    public static TrashCanInfo of(Long trashCanId, Double lng, Double lat, String address, String locationName, String trashType) {
        return TrashCanInfo.builder()
                .trashCanId(trashCanId)
                .lng(lng)
                .lat(lat)
                .address(address)
                .trashType(trashType)
                .locationName(locationName)
                .build();
    }
}
