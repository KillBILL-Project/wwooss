package com.bigbro.wwooss.v1.dto.response.trash;

import com.bigbro.wwooss.v1.entity.trash.info.TrashInfo;
import com.bigbro.wwooss.v1.enumType.TrashSize;
import com.bigbro.wwooss.v1.enumType.TrashType;
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
    private TrashType trashCategoryName;

    // 환불 금액
    private Long refund;

    // 크기
    private TrashSize size;

    // image
    private String trashImagePath;

    public static TrashInfoResponse of(TrashInfo trashInfo, String baseImagePath) {
        return TrashInfoResponse.builder()
                .trashInfoId(trashInfo.getTrashInfoId())
                .trashCategoryName(trashInfo.getTrashCategory().getTrashType())
                .refund(trashInfo.getRefund())
                .size(trashInfo.getSize())
                .trashImagePath(baseImagePath + trashInfo.getTrashImagePath())
                .build();
    }
}
