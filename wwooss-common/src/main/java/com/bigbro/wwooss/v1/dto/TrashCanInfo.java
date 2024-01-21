package com.bigbro.wwooss.v1.dto;

import com.bigbro.wwooss.v1.enumType.TrashType;
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

    // , 로 구분 => PLASTIC,CAN,PAPER
    private String trashType;

    public static TrashCanInfo of(Long trashCanId, Double lng, Double lat, String address, String trashType) {
        return TrashCanInfo.builder()
                .trashCanId(trashCanId)
                .lng(lng)
                .lat(lat)
                .address(address)
                .trashType(trashType)
                .build();
    }
}
