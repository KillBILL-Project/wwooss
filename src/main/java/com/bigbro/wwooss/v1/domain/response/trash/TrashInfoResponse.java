package com.bigbro.wwooss.v1.domain.response.trash;

import com.bigbro.wwooss.v1.domain.entity.trash.info.TrashInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashInfoResponse {

    private Long trashInfoId;

    // 쓰레기 이름
    private String name;

    // 쓰레기 무게 (gram)
    private Double weight;

    // 1그램당 탄소 배출량
    private Double carbonEmissionPerGram;

    // 환불 금액
    private Integer refund;

    public static TrashInfoResponse from(TrashInfo trashInfo) {
        return TrashInfoResponse.builder()
                .trashInfoId(trashInfo.getTrashInfoId())
                .name(trashInfo.getName())
                .weight(trashInfo.getWeight())
                .carbonEmissionPerGram(trashInfo.getCarbonEmissionPerGram())
                .refund(trashInfo.getRefund())
                .build();
    }
}
