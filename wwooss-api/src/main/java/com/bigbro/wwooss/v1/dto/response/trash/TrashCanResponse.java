package com.bigbro.wwooss.v1.dto.response.trash;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanResponse {

    private Long trashCanId;

    private Double lng;

    private Double lat;

    private String address;

    // , 로 구분 => PAPER[종이] / CAN[캔] / PLASTIC[플라스틱] / PET[페트병] / GLASS[병] / VINYL[비닐] / VASSEL[빈용기] / COMMON[기타]
    private List<String> trashType;

    public static TrashCanResponse of(Long trashCanId, Double lat, Double lng, String address, List<String> trashType) {
        return TrashCanResponse.builder()
                .trashCanId(trashCanId)
                .lat(lat)
                .lng(lng)
                .address(address)
                .trashType(trashType)
                .build();
    }
}
