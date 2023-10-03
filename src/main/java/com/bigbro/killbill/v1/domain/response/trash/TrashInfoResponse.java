package com.bigbro.killbill.v1.domain.response.trash;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import com.bigbro.killbill.v1.domain.entity.trash.TrashInfoEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashInfoResponse {

    private Long trashInfoId;

    // 쓰레기 이름
    private String name;

    // 쓰레기 사이즈
    private Integer size;

    // 쓰레기 무게 (gram)
    private Double weight;

    // 1그램당 탄소 배출량
    private Double carbonEmissionPerGram;

    // 환불 금액
    private Integer refund;

    public static TrashInfoResponse from(TrashInfoEntity trashInfoEntity) {
        return TrashInfoResponse.builder()
                .trashInfoId(trashInfoEntity.getTrashInfoId())
                .name(trashInfoEntity.getName())
                .size(trashInfoEntity.getSize())
                .weight(trashInfoEntity.getWeight())
                .carbonEmissionPerGram(trashInfoEntity.getCarbonEmissionPerGram())
                .refund(trashInfoEntity.getRefund())
                .build();
    }
}
